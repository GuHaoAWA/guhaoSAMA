package com.guhao.renderers;


import com.dfdyz.epicacg.client.render.EpicACGRenderType;
import com.dfdyz.epicacg.client.render.custom.BloomParticleRenderType;
import com.google.common.collect.Maps;
import com.guhao.utils.RenderUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class GuHaoRenderType {
    private static int bloomIdx = 0;
    public static final HashMap<ResourceLocation, BloomParticleRenderType> BloomRenderTypes = Maps.newHashMap();
    private static int quadIdx = 0;
    public static final HashMap<ResourceLocation, EpicACGRenderType.EpicACGQuadParticleRenderType> QuadRenderTypes = Maps.newHashMap();

    public static final ResourceLocation NoneTexture = RenderUtils.GetTexture("none");
    public static final ParticleRenderType TRANSLUCENT = new ParticleRenderType() {
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);
            RenderSystem.disableTexture();
            RenderUtils.GLSetTexture(EpicACGRenderType.NoneTexture);
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        }

        public void end(Tesselator tesselator) {
            tesselator.getBuilder().setQuadSortOrigin(0.0F, 0.0F, 0.0F);
            tesselator.end();
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableCull();
        }

        public String toString() {
            return "GUHAO:TRANSLUCENT";
        }
    };

    public GuHaoRenderType() {
    }

    public static BloomParticleRenderType getBloomRenderTypeByTexture(ResourceLocation texture) {
        if (BloomRenderTypes.containsKey(texture)) {
            return BloomRenderTypes.get(texture);
        } else {
            int var10005 = bloomIdx++;
            BloomParticleRenderType bloomType = new BloomParticleRenderType(new ResourceLocation("guhao", "bloom_particle_" + var10005), texture);
            BloomRenderTypes.put(texture, bloomType);
            return bloomType;
        }
    }

    public static EpicACGRenderType.EpicACGQuadParticleRenderType getRenderTypeByTexture(ResourceLocation texture) {
        if (QuadRenderTypes.containsKey(texture)) {
            return (EpicACGRenderType.EpicACGQuadParticleRenderType)QuadRenderTypes.get(texture);
        } else {
            EpicACGRenderType.EpicACGQuadParticleRenderType rdt = new EpicACGRenderType.EpicACGQuadParticleRenderType("guhao:quad_particle_" + quadIdx++, texture);
            QuadRenderTypes.put(texture, rdt);
            return rdt;
        }
    }

    public static ShaderInstance getPositionColorLightmapShader() {
        return GameRenderer.getPositionColorLightmapShader();
    }

    public static class EpicACGQuadParticleRenderType implements ParticleRenderType {
        private final ResourceLocation Texture;
        private final String Name;

        public EpicACGQuadParticleRenderType(String name, ResourceLocation tex) {
            this.Texture = tex;
            this.Name = name;
        }

        public void begin(BufferBuilder p_107448_, TextureManager p_107449_) {
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.setShader(GameRenderer::getParticleShader);
            if (this.Texture != null) {
                RenderUtils.GLSetTexture(this.Texture);
            }

            p_107448_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public void end(Tesselator tesselator) {
            tesselator.getBuilder().setQuadSortOrigin(0.0F, 0.0F, 0.0F);
            tesselator.end();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableCull();
        }

        public String toString() {
            return this.Name;
        }
    }
}
