package com.guhao.init;

import com.guhao.client.particle.par.EyeParticle;
import com.guhao.client.particle.par.RingParticle;
import com.guhao.client.particle.par.TrailParticleGuhao;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.particle.HitParticleType;

import static com.guhao.Guhao.MODID;


public class ParticleType {
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLES;
    public static final RegistryObject<SimpleParticleType> TRAIL_GUHAO;
    public static final RegistryObject<SimpleParticleType> EYE;
    public static final RegistryObject<SimpleParticleType> RING;

    @OnlyIn(Dist.CLIENT)
    public static void RP(ParticleFactoryRegisterEvent event) {
        ParticleEngine PE = Minecraft.getInstance().particleEngine;
        PE.register((net.minecraft.core.particles.ParticleType<SimpleParticleType>) TRAIL_GUHAO.get(), TrailParticleGuhao.Provider::new);
        PE.register((net.minecraft.core.particles.ParticleType<SimpleParticleType>) EYE.get(), EyeParticle.EyeParticleProvider::new);
        PE.register((net.minecraft.core.particles.ParticleType<SimpleParticleType>) RING.get(), RingParticle.RingParticleProvider::new);
    }

    public ParticleType() {
    }

    static {
        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);
        TRAIL_GUHAO = PARTICLES.register("trail_guhao", () -> {
            return new SimpleParticleType(true);
        });
        EYE = PARTICLES.register("eye", () -> {
            return new HitParticleType(true, HitParticleType.RANDOM_WITHIN_BOUNDING_BOX, HitParticleType.ZERO);
        });
        RING = PARTICLES.register("ring", () -> {
            return new SimpleParticleType(true);
        });
    }
}
