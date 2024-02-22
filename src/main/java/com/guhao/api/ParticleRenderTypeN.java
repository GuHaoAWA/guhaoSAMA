package com.guhao.api;

import com.dfdyz.epicacg.client.render.custom.BloomParticleRenderType;
import com.dfdyz.epicacg.utils.RenderUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import static com.guhao.Guhao.MODID;

public interface ParticleRenderTypeN {
    ParticleRenderType PARTICLE_SHEET_LIT_NO_CULL = new BloomParticleRenderType(new ResourceLocation(MODID, "ring"), RenderUtils.GetTexture("particle/ring")) {
        public void begin(BufferBuilder p_107462_, TextureManager p_107463_) {
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.disableCull();
            RenderSystem.setShaderTexture(1, 1);
            p_107462_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }


        public void end(Tesselator p_107465_) {
            p_107465_.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_LIT_NO_CULL";
        }
    };
}

