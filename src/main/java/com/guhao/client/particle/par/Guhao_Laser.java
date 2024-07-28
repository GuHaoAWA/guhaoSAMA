package com.guhao.client.particle.par;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.particle.CustomModelParticle;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;

public class Guhao_Laser extends CustomModelParticle<Mesh.RawMesh> {
    private final float length;
    private final float xRot;
    private final float yRot;

    public Guhao_Laser(ClientLevel level, double x, double y, double z, double toX, double toY, double toZ) {
        super(level, x, y, z, 0.0, 0.0, 0.0, Meshes.LASER);
        this.lifetime = 6;
        Vec3 direction = new Vec3(toX - x, toY - y, toZ - z);
        Vec3 start = new Vec3(x, y, z);
        Vec3 destination = start.add(direction.normalize().scale(200.0));
        BlockHitResult hitResult = level.clip(new ClipContext(start, destination, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        double xLength = hitResult.getLocation().x - x;
        double yLength = hitResult.getLocation().y - y;
        double zLength = hitResult.getLocation().z - z;
        double horizontalDistance = (float)Math.sqrt(xLength * xLength + zLength * zLength);
        this.length = (float)Math.sqrt(xLength * xLength + yLength * yLength + zLength * zLength);
        this.yRot = (float)(-Math.atan2(zLength, xLength) * 57.29577951308232) - 90.0F;
        this.xRot = (float)(Math.atan2(yLength, horizontalDistance) * 57.29577951308232);
        int smokeCount = (int)this.length * 4;

        for(int i = 0; i < smokeCount; ++i) {
            level.addParticle(ParticleTypes.SMOKE, x + xLength / (double)smokeCount * (double)i, y + yLength / (double)smokeCount * (double)i, z + zLength / (double)smokeCount * (double)i, 0.0, 0.0, 0.0);
        }

        this.setBoundingBox(new AABB(x, y, z, toX, toY, toZ));
    }

    public void prepareDraw(PoseStack poseStack, float partialTicks) {
        poseStack.mulPose(Vector3f.YP.rotationDegrees(this.yRot));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(this.xRot));
        float progression = ((float)this.age + partialTicks) / (float)(this.lifetime + 1);
        float scale = Mth.sin(progression * 3.1415927F);
        float zScale = progression > 0.5F ? 1.0F : Mth.sin(progression * 3.1415927F);
        float sc = 2.0f;
        poseStack.scale(scale* sc, scale* sc, (zScale * this.length)* sc);
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        super.render(vertexConsumer, camera, partialTicks);
        PoseStack poseStack = new PoseStack();
        this.setupPoseStack(poseStack, camera, partialTicks);
        this.prepareDraw(poseStack, partialTicks);
        poseStack.scale(1.1F, 1.1F, 1.1F);
    }

    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.TRANSLUCENT_GLOWING;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider(SpriteSet spriteSet) {
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double startX, double startY, double startZ, double endX, double endY, double endZ) {
            Guhao_Laser particle = new Guhao_Laser(level, startX, startY, startZ, endX, endY, endZ);
            particle.setColor(255f, 0f, 0f);
            particle.setAlpha(50.0f);
            return particle;
        }
    }
}
