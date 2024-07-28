package com.guhao.client.particle.par;

import com.dfdyz.epicacg.client.render.EpicACGRenderType;
import com.dfdyz.epicacg.client.render.pipeline.PostEffectPipelines;
import com.guhao.api.ParticleRenderTypeN;
import com.guhao.utils.RenderUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class BloodFireParticle extends TextureSheetParticle {
    final static float min = 0.15f;
    final static float max = 1.50f;
    Random r = new Random();
    private final Vector3f vector;
    private final int style;
    public static BloodFireParticleProvider provider(SpriteSet spriteSet) {
        return new BloodFireParticleProvider(spriteSet);
    }

    public static class BloodFireParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public BloodFireParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BloodFireParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;

    protected BloodFireParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;


        this.vector = new Vector3f(this.random.nextFloat(2.0F) - 1.0F, this.random.nextFloat(2.0F) - 1.0F, this.random.nextFloat(2.0F) - 1.0F);
        this.vector.normalize();
        this.style = this.random.nextInt(8);


        this.setSize(0f, 0f);
        this.quadSize *= (min + (r.nextFloat() * (max - min)));
        this.lifetime = 64;

        this.gravity = -0.09f;
        this.hasPhysics = false;

        this.xd = vx * 1;
        this.yd = vy * 1;
        this.zd = vz * 1;
        this.pickSprite(spriteSet);

    }
    public void render(VertexConsumer vertexBuffer, Camera camera, float pt) {
            Vec3 vec3 = camera.getPosition();
            float f = (float)(Mth.lerp((double)pt, this.xo, this.x) - vec3.x());
            float f1 = (float)(Mth.lerp((double)pt, this.yo, this.y) - vec3.y());
            float f2 = (float)(Mth.lerp((double)pt, this.zo, this.z) - vec3.z());
            Quaternion quaternion;
            if (this.roll == 0.0F) {
                quaternion = camera.rotation();
            } else {
                quaternion = new Quaternion(camera.rotation());
                float f3 = Mth.lerp(pt, this.oRoll, this.roll);
                quaternion.mul(Vector3f.ZP.rotation(f3));
            }

            Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
            vector3f1.transform(quaternion);
            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float f4 = this.getQuadSize(pt);

            for(int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.transform(quaternion);
                vector3f.mul(f4);
                vector3f.add(f, f1, f2);
            }

            float f7 = this.getU0();
            float f8 = this.getU1();
            float f5 = this.getV0();
            float f6 = this.getV1();
            int j = 15728880;
            vertexBuffer.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexBuffer.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexBuffer.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
            vertexBuffer.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();

    }
    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderTypeN.PARTICLE_SHEET_LIT_NO_CULL;
    }
    @Override
    public void tick() {
        super.tick();
    }
}

