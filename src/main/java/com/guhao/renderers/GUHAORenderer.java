package com.guhao.renderers;

import com.guhao.api.RendersPlayerArms;
import com.guhao.init.Effect;
import com.guhao.init.ParticleType;
import com.guhao.item.GUHAO;
import com.guhao.item.model.GUHAOModel;
import com.guhao.utils.AnimUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("deprecated")
//壁虎立体阿尔法通道渐变变色呼吸灯式增添关键帧事件的mc事例判断的动态变换模型的动态模型和动态动画的动画搭配if判断的动态发光贴图联动efm的模型
public class GUHAORenderer extends GeoItemRenderer<GUHAO> implements RendersPlayerArms, AnimationController.IParticleListener<GUHAO>, IAnimatable, ISyncable {
	public GUHAORenderer() {
		super(new GUHAOModel());
	}
	private static final String CONTROLLER_NAME = "GuHaoController";
	@Override
	public RenderType getRenderType(GUHAO animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, ResourceLocation texture) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	static {
		AnimationController.addModelFetcher(animatable -> {
			if (animatable instanceof GUHAO item) {
                GeoItemRenderer<?> ister = new GUHAORenderer();
				return (IAnimatableModel<Object>) ister.getGeoModelProvider();
			}
			return null;
		});
	}
	private static final float SCALE_RECIPROCAL = 1.0f / 16.0f;
	protected boolean renderArms = false;
	protected MultiBufferSource currentBuffer;
	protected RenderType renderType;
	public TransformType transformType;
	protected GUHAO animatable;
	private float aimProgress = 0.0f;
	private final Set<String> hiddenBones = new HashSet<>();
	private final Set<String> suppressedBones = new HashSet<>();
	private final Map<String, Vector3f> queuedBoneSetMovements = new HashMap<>();
	private final Map<String, Vector3f> queuedBoneSetRotations = new HashMap<>();
	private final Map<String, Vector3f> queuedBoneAddRotations = new HashMap<>();

	@Override
	public void renderByItem(ItemStack itemStack, TransformType transformType, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int p_239207_6_) {
		this.transformType = transformType;
		super.renderByItem(itemStack, transformType, matrixStack, bufferIn, combinedLightIn, p_239207_6_);
	}

	@Override
	public void render(GeoModel model, GUHAO animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		packedLightIn = 0xf000ff;
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		Minecraft mc = Minecraft.getInstance();
		float sign = 1.0f;
		this.aimProgress = Mth.clamp(this.aimProgress + mc.getFrameTime() * sign * 0.1f, 0.0f, 1.0f);
		alpha = 112.5f;
		float minAlpha = 24.0f; // 最小alpha值
		float maxAlpha = 255.0f; // 最大alpha值
		float minRed = 0.0f; // 最小Red值
		float minGreen = 30.0f; //
		float minBlue = 30.0f; //
		float maxRed = 255.0f; // 最大Red值
		float maxGreen = 255.0f; //
		float maxBlue = 255.0f; //
		float cycleTime = 10f; // 一个呼吸灯循环的速率
		float time = mc.getFrameTime();
		float progress = Mth.clamp(time % cycleTime / cycleTime, 0.0f, 1.0f);
		//float progress = Mth.clamp((time % cycleTime) / cycleTime, 0.0f, 1.0f);
		alpha = Mth.lerp(progress, minAlpha, maxAlpha);
		/*
		if (alpha >= maxAlpha-1) {
			minAlpha = maxAlpha;
			maxAlpha = minAlpha;
		}
		else if (alpha <= minAlpha+1) {
			minAlpha = maxAlpha;
			maxAlpha = minAlpha;
		}
		alpha = Mth.lerp(progress, minAlpha, maxAlpha);
		 */
		if (mc.player != null && mc.player.hasEffect(Effect.GUHAO.get())) {
			//red = Mth.lerp(progress, minRed, maxRed);
			green = Mth.lerp(progress, minGreen, maxGreen);
			blue = Mth.lerp(progress, minBlue, maxBlue);
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////
		this.currentBuffer = renderTypeBuffer;
		this.renderType = type;
		this.animatable = animatable;
		super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		if (this.renderArms) {
			this.renderArms = false;
		}
		if (this.animatable != null)
			this.animatable.getTransformType(this.transformType);
	}
	@Override
	public void render(GUHAO animatable, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, ItemStack itemStack) {
		Minecraft mc = Minecraft.getInstance();
		float sign = 1.0f;
		this.aimProgress = Mth.clamp(this.aimProgress + mc.getFrameTime() * sign * 0.1f, 0.0f, 1.0f);
		stack.pushPose();
		animatable.setupAnimationState(this, itemStack, stack, this.aimProgress);
		super.render(animatable, stack, bufferIn, packedLightIn, itemStack);
		stack.popPose();
		if (this.animatable != null)
			this.animatable.getTransformType(this.transformType);
	}

	@Override
	public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		Minecraft mc = Minecraft.getInstance();
		String name = bone.getName();
		boolean renderingArms = false;
		if (name.equals("") || name.equals("")) {
			bone.setHidden(true);
			renderingArms = true;
		} else {
			bone.setHidden(this.hiddenBones.contains(name));
		}
		if (!this.suppressedBones.contains(name)) {
			if (this.queuedBoneSetMovements.containsKey(name)) {
				Vector3f pos = this.queuedBoneSetMovements.get(name);
				bone.setPositionX(pos.x());
				bone.setPositionY(pos.y());
				bone.setPositionZ(pos.z());
			}
			if (this.queuedBoneSetRotations.containsKey(name)) {
				Vector3f rot = this.queuedBoneSetRotations.get(name);
				bone.setRotationX(rot.x());
				bone.setRotationY(rot.y());
				bone.setRotationZ(rot.z());
			}
			if (this.queuedBoneAddRotations.containsKey(name)) {
				Vector3f rot = this.queuedBoneAddRotations.get(name);
				bone.setRotationX(bone.getRotationX() + rot.x());
				bone.setRotationY(bone.getRotationY() + rot.y());
				bone.setRotationZ(bone.getRotationZ() + rot.z());
			}
		}
		if (this.transformType.firstPerson() && renderingArms) {
			AbstractClientPlayer player = mc.player;
			float armsAlpha = player.isInvisible() ? 0.15f : 1.0f;
			PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
			PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
			stack.pushPose();
			ResourceLocation loc = player.getSkinTextureLocation();
			VertexConsumer armBuilder = this.currentBuffer.getBuffer(RenderType.entitySolid(loc));
			VertexConsumer sleeveBuilder = this.currentBuffer.getBuffer(RenderType.entityTranslucent(loc));
			if (name.equals("")) {
				stack.translate(-1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				AnimUtils.renderPartOverBone(model.leftArm, bone, stack, armBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, armsAlpha);
				AnimUtils.renderPartOverBone(model.leftSleeve, bone, stack, sleeveBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, armsAlpha);
			} else if (name.equals("")) {
				stack.translate(1.0f * SCALE_RECIPROCAL, 2.0f * SCALE_RECIPROCAL, 0.0f);
				AnimUtils.renderPartOverBone(model.rightArm, bone, stack, armBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, armsAlpha);
				AnimUtils.renderPartOverBone(model.rightSleeve, bone, stack, sleeveBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, armsAlpha);
			}
			stack.popPose();
		}
		super.renderRecursively(bone, stack, this.currentBuffer.getBuffer(this.renderType), packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	@Override
	public ResourceLocation getTextureLocation(GUHAO instance) {
		return super.getTextureLocation(instance);
	}


	public void hideBone(String name, boolean hide) {
		if (hide) {
			this.hiddenBones.add(name);
		} else {
			this.hiddenBones.remove(name);
		}
	}

	@Override
	public void setRenderArms(boolean renderArms) {
		this.renderArms = renderArms;
	}

	public TransformType getCurrentTransform() {
		return this.transformType;
	}

	public void suppressModification(String name) {
		this.suppressedBones.add(name);
	}

	public void allowModification(String name) {
		this.suppressedBones.remove(name);
	}

	public void setBonePosition(String name, float x, float y, float z) {
		this.queuedBoneSetMovements.put(name, new Vector3f(x, y, z));
	}

	public void addToBoneRotation(String name, float x, float y, float z) {
		this.queuedBoneAddRotations.put(name, new Vector3f(x, y, z));
	}

	public void setBoneRotation(String name, float x, float y, float z) {
		this.queuedBoneSetRotations.put(name, new Vector3f(x, y, z));
	}

	public ItemStack getCurrentItem() {
		return this.currentItemStack;
	}

	@Override
	public boolean shouldAllowHandRender(ItemStack mainhand, ItemStack offhand, InteractionHand renderingHand) {
		return renderingHand == InteractionHand.MAIN_HAND;
	}

	@Override
	public void summonParticle(ParticleKeyFrameEvent particleKeyFrameEvent) {
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController controller = new AnimationController(this, CONTROLLER_NAME, 86, this::predicate);
		controller.registerSoundListener(this::particleListener);
		data.addAnimationController(controller);
	}

	private void particleListener(SoundKeyframeEvent soundKeyframeEvent) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && mc.player.getLevel() instanceof ServerLevel world){
			world.sendParticles(ParticleType.TWO_EYE.get(), mc.player.getX(), mc.player.getY(), mc.player.getZ(), 1, 0.5, 0.5, 0.5, 0);
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return null;
	}

	@Override
	public void onAnimationSync(int i, int i1) {

	}
	private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
		return PlayState.CONTINUE;
	}

}
