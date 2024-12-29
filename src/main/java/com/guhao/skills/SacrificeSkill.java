package com.guhao.skills;

import com.google.common.collect.Maps;
import com.guhao.GuHaoAnimations;
import com.guhao.init.Effect;
import com.guhao.init.Key;
import com.guhao.star.efmex.StarAnimations;
import com.nameless.falchion.gameasset.FalchionAnimations;
import io.netty.buffer.Unpooled;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Map;
import java.util.UUID;

public class SacrificeSkill extends WeaponInnateSkill {
    private final StaticAnimation[] animations;
    private final Vec3 stop = new Vec3(0.07,0.07,0.07);
    public final Map<StaticAnimation, AttackAnimation> comboAnimation = Maps.newHashMap();
    private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc49-b83e-16ae590db349");
    public static SkillDataManager.SkillDataKey<Boolean> IS_CTRL_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);

    public SacrificeSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
        this.animations = new StaticAnimation[]{FalchionAnimations.FALCHION_FORWARD, FalchionAnimations.FALCHION_BACKWARD, FalchionAnimations.FALCHION_SIDE};
        this.comboAnimation.put(Animations.TACHI_AUTO3, (AttackAnimation) Animations.RUSHING_TEMPO3);
        this.comboAnimation.put(Animations.UCHIGATANA_AUTO1, (AttackAnimation) Animations.RUSHING_TEMPO1);
        this.comboAnimation.put(Animations.UCHIGATANA_AUTO3, (AttackAnimation) Animations.RUSHING_TEMPO2);
        this.comboAnimation.put(Animations.LONGSWORD_AUTO2, (AttackAnimation) StarAnimations.KATANA_SHEATH_DASH.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
        this.comboAnimation.put(GuHaoAnimations.GUHAO_BIU, (AttackAnimation) GuHaoAnimations.BIU.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
        this.comboAnimation.put(GuHaoAnimations.GUHAO_DASH_2, (AttackAnimation) GuHaoAnimations.DENG_LONG.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));

        this.comboAnimation.put(Animations.RUSHING_TEMPO3, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
        this.comboAnimation.put(Animations.RUSHING_TEMPO1, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
        this.comboAnimation.put(Animations.RUSHING_TEMPO2, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
        this.comboAnimation.put(StarAnimations.KATANA_SHEATH_DASH, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
        this.comboAnimation.put(GuHaoAnimations.BIU, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
        this.comboAnimation.put(GuHaoAnimations.SETTLEMENT, (AttackAnimation) GuHaoAnimations.DENG_LONG.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));


        this.comboAnimation.put(Animations.REVELATION_TWOHAND, (AttackAnimation) GuHaoAnimations.GUHAO_UCHIGATANA_SCRAP.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
    }
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(IS_CTRL_DOWN);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
            ServerPlayerPatch executer = event.getPlayerPatch();
            DynamicAnimation animation = executer.getAnimator().getPlayerFor(null).getAnimation();
            if (animation == FalchionAnimations.FALCHION_SIDE || animation == FalchionAnimations.FALCHION_AUTO3) {
                event.getDamageSource().setStunType(StunType.NONE);
            }
        });

    }
    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID);
    }
    @Override
    public WeaponInnateSkill registerPropertiesToAnimation() {
        this.comboAnimation.values().forEach((animation) -> animation.phases[0].addProperties(this.properties.get(0).entrySet()));
        return this;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public FriendlyByteBuf gatherArguments(LocalPlayerPatch executer, ControllEngine controllEngine) {
        Input input = executer.getOriginal().input;
        input.tick(false);
        int forward = input.up ? 1 : 0;
        int backward = input.down ? -1 : 0;
        int left = input.left ? 1 : 0;
        int right = input.right ? -1 : 0;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(forward);
        buf.writeInt(backward);
        buf.writeInt(left);
        buf.writeInt(right);
        return buf;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object getExecutionPacket(LocalPlayerPatch executer, FriendlyByteBuf args) {
        int forward = args.readInt();
        int backward = args.readInt();
        int left = args.readInt();
        int right = args.readInt();
        int vertic = forward + backward;
        int horizon = left + right;
        int animation;
        if (vertic == 0) {
            if (horizon == 0) {
                animation = 1;
            } else {
                animation = 2;
            }
        } else {
            animation = vertic >= 0 ? 0 : 1;
        }

        CPExecuteSkill packet = new CPExecuteSkill(executer.getSkill(this).getSlotId());
        packet.getBuffer().writeInt(animation);
        return packet;
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if(container.getExecuter().isLogicalClient()) {
            container.getDataManager().setDataSync(IS_CTRL_DOWN, Key.CTRL.isDown(), ((LocalPlayer) container.getExecuter().getOriginal()));
        }
    }

    /**
     * 保险
     */
    public static void register(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            IS_CTRL_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        });
    }

    //    @OnlyIn(Dist.CLIENT)
//    @Override
//    public boolean shouldDraw(SkillContainer container) {
//        PlayerPatch<?> executer = container.getExecuter();
//        EntityState playerState = executer.getEntityState();
//        return this.comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation().getRegistryName()) && playerState.canUseSkill() && playerState.inaction();
//    }
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void drawOnGui(BattleModeGui gui, SkillContainer container, PoseStack poseStack, float x, float y) {
//        poseStack.pushPose();
//        poseStack.translate(0.0, (float)gui.getSlidingProgression(), 0.0);
//        ResourceLocation name = this.getRegistryName();
//        RenderSystem.setShaderTexture(0, new ResourceLocation(name.getNamespace(), "textures/gui/skills/guhao_passive.png"));
//        GuiComponent.blit(poseStack, (int)x, (int)y, 24, 24, 0, 0, 1, 1, 1, 1);
//        poseStack.popPose();
//    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(GuHaoPassive.SHEATH);
        boolean isStop = executer.getOriginal().isSprinting();
        boolean isOnGround = executer.getOriginal().isOnGround();
        while (true) {
            if (this.comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation())) {
                executer.playAnimationSynchronized(this.comboAnimation.get(executer.getAnimator().getPlayerFor(null).getAnimation()), 0.0F);
                super.executeOnServer(executer, args);
                break;
            }
            if (executer.getOriginal().isShiftKeyDown() && (executer.getSkill(GuHaoSkills.SACRIFICE).getStack() >= 10)) {
                if (executer.getOriginal().hasEffect(Effect.GUHAO.get())) {
                    if (isSheathed) {
                        executer.playAnimationSynchronized(GuHaoAnimations.BLOOD_JUDGEMENT, -0.3F);
                        executer.getSkill(GuHaoSkills.SACRIFICE).setStack(4);
                        executer.setStamina(executer.getStamina() * 0.66F);
                        super.executeOnServer(executer, args);
                    } else {
                        executer.playAnimationSynchronized(GuHaoAnimations.BLOOD_JUDGEMENT, 0.0F);
                        executer.getSkill(GuHaoSkills.SACRIFICE).setStack(3);
                        executer.setStamina(executer.getStamina() * 0.5F);
                        super.executeOnServer(executer, args);
                    }
                } else {
                    executer.playAnimationSynchronized(GuHaoAnimations.SACRIFICE, 0.0F);
                    executer.getSkill(GuHaoSkills.SACRIFICE).setStack(0);
                    executer.setStamina(0.0F);
                    super.executeOnServer(executer, args);
                }
                break;
            }
///////////////////////////////////////////////////////////////////////////
            if (executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(IS_CTRL_DOWN)) {
                int i = args.readInt();
                executer.playAnimationSynchronized(this.animations[i], 0.0F);
                super.executeOnServer(executer, args);
                break;
            }
///////////////////////////////////////////////////////////////////////////
            if (isOnGround && !isStop && isSheathed) {
                executer.playAnimationSynchronized(GuHaoAnimations.SETTLEMENT, 0.0F);
                super.executeOnServer(executer, args);
            } else {
                    if (isSheathed) {
                        executer.playAnimationSynchronized(GuHaoAnimations.GUHAO_BATTOJUTSU_DASH, -0.694F);
                        super.executeOnServer(executer, args);
                    } else {
                        executer.playAnimationSynchronized(GuHaoAnimations.GUHAO_BATTOJUTSU_DASH, 0.0F);
                        super.executeOnServer(executer, args);
                    }
            }
            break;
        }
    }
}