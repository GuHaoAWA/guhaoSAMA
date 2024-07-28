package com.guhao.client.particle.par;

import com.dfdyz.epicacg.client.render.EpicACGRenderType;
import com.dfdyz.epicacg.client.render.IPatchedAnimatedMesh;
import com.guhao.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.particle.CustomModelParticle;
import yesman.epicfight.client.renderer.patched.entity.PatchedEntityRenderer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.awt.*;

public class After_Image_BloodParticle extends CustomModelParticle<AnimatedMesh> {
    private OpenMatrix4f[] poseMatrices;
    private Matrix4f modelMatrix;
    private float alphaO;

    public After_Image_BloodParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, AnimatedMesh particleMesh, OpenMatrix4f[] matrices, Matrix4f modelMatrix) {
        super(level, x, y, z, xd, yd, zd, particleMesh);
        this.poseMatrices = matrices;
        this.modelMatrix = modelMatrix;
        this.lifetime = 20;
        this.rCol = 1.0F; // 红色红色分量
        this.gCol = 1.0F; // 红色绿色分量
        this.bCol = 1.0F; // 红色蓝色分量
        this.alphaO = 0.3F;
        this.alpha = 0.3F;
    }
    @Override
    public boolean shouldCull() {
        return false;
    }
    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.pitchO = this.pitch;
            this.yawO = this.yaw;
            this.oRoll = this.roll;
            this.scaleO = this.scale;
        }
    }
    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        poseStack.mulPoseMatrix(this.modelMatrix);
        ((IPatchedAnimatedMesh)this.particleMesh).drawWithPoseNoTexture2(poseStack, vertexConsumer, RenderUtils.DefaultLightColor,
                this.rCol, this.gCol, this.bCol, alpha,
                OverlayTexture.NO_OVERLAY, this.poseMatrices);
    }
    @Override
    public ParticleRenderType getRenderType() {
        return EpicACGRenderType.TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet spriteSet) {

        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Entity entity = level.getEntity((int)Double.doubleToLongBits(xSpeed));
            Color color = new Color((int)Double.doubleToRawLongBits(ySpeed), true);
            LivingEntityPatch<?> entitypatch = (LivingEntityPatch)EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            if (entitypatch != null && ClientEngine.getInstance().renderEngine.hasRendererFor(entitypatch.getOriginal())) {
                PatchedEntityRenderer renderer = ClientEngine.getInstance().renderEngine.getEntityRenderer(entitypatch.getOriginal());
                Armature armature = entitypatch.getArmature();
                PoseStack poseStack = new PoseStack();
                OpenMatrix4f[] matrices = renderer.getPoseMatrices(entitypatch, armature, 1.0F);
                renderer.mulPoseStack(poseStack, armature, entitypatch.getOriginal(), entitypatch, 1.0F);
                for(int i = 0; i < matrices.length; ++i) {
                    matrices[i] = OpenMatrix4f.mul(matrices[i], armature.searchJointById(i).getToOrigin(), (OpenMatrix4f)null);
                }
                AnimatedMesh mesh = ClientEngine.getInstance().renderEngine.getEntityRenderer(entitypatch.getOriginal()).getMesh(entitypatch);
                After_Image_BloodParticle particle = new After_Image_BloodParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, mesh, matrices, poseStack.last().pose());
                particle.setColor(12.4f, 0.12f, 0.15f);
                return particle;
            }
                return null;
        }
    }
}