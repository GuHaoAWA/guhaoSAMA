package com.guhao.client.particle.par;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BloodFireParticle extends RisingParticle {
    /*
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

     */
    public BloodFireParticle(ClientLevel p_106800_, double p_106801_, double p_106802_, double p_106803_, double p_106804_, double p_106805_, double p_106806_) {
        super(p_106800_, p_106801_, p_106802_, p_106803_, p_106804_, p_106805_, p_106806_);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void move(double p_106817_, double p_106818_, double p_106819_) {
        this.setBoundingBox(this.getBoundingBox().move(p_106817_, p_106818_, p_106819_));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(float p_106824_) {
        float $$1 = ((float)this.age + p_106824_) / (float)this.lifetime;
        return this.quadSize * (1.0F - $$1 * $$1 * 0.5F);
    }

    public int getLightColor(float p_106821_) {
        float $$1 = ((float)this.age + p_106821_) / (float)this.lifetime;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        int $$2 = super.getLightColor(p_106821_);
        int $$3 = $$2 & 255;
        int $$4 = $$2 >> 16 & 255;
        $$3 += (int)($$1 * 15.0F * 16.0F);
        if ($$3 > 240) {
            $$3 = 240;
        }

        return $$3 | $$4 << 16;
    }

    @OnlyIn(Dist.CLIENT)
    public static class SmallFlameProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        public SmallFlameProvider(SpriteSet p_172113_) {
            this.sprite = p_172113_;
        }

        public Particle createParticle(SimpleParticleType p_172124_, ClientLevel p_172125_, double p_172126_, double p_172127_, double p_172128_, double p_172129_, double p_172130_, double p_172131_) {
            BloodFireParticle $$8 = new BloodFireParticle(p_172125_, p_172126_, p_172127_, p_172128_, p_172129_, p_172130_, p_172131_);
            $$8.pickSprite(this.sprite);
            $$8.scale(0.5F);
            return $$8;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet p_106827_) {
            this.sprite = p_106827_;
        }

        public Particle createParticle(SimpleParticleType p_106838_, ClientLevel p_106839_, double p_106840_, double p_106841_, double p_106842_, double p_106843_, double p_106844_, double p_106845_) {
            BloodFireParticle $$8 = new BloodFireParticle(p_106839_, p_106840_, p_106841_, p_106842_, p_106843_, p_106844_, p_106845_);
            $$8.pickSprite(this.sprite);
            return $$8;
        }
    }
}

