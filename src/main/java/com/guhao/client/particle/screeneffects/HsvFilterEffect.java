package com.guhao.client.particle.screeneffects;


import com.guhao.init.PostEffects;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import static com.guhao.Guhao.MODID;

public class HsvFilterEffect extends ScreenEffectBase {
    public enum Type{
        PREV, POST
    }

    public Type type = Type.POST;

    public HsvFilterEffect(Vec3 pos) {
        super(hsv_filter, pos);
        this.ppl = new HsvFilter_Pipeline(this);
        this.lifetime = 40;
    }
    static ResourceLocation hsv_filter = new ResourceLocation(MODID, "hsv_filter");
    public final SE_Pipeline ppl;

    @Override
    public boolean shouldPost(Camera camera, Frustum clippingHelper) {
        return pos.subtract(camera.getPosition()).length() < 32;
    }

    public static class HsvFilter_Pipeline extends SE_Pipeline<HsvFilterEffect>{
        public HsvFilter_Pipeline(HsvFilterEffect effect) {
            super(hsv_filter, effect);
            priority = 101;
        }
        static ResourceLocation hsv_filter_tmp = new ResourceLocation(MODID, "hsv_filter_tmp");
        @Override
        public void PostEffectHandler() {
            RenderTarget tmp = TargetManager.getTarget(hsv_filter_tmp);
            PostEffects.blit.process(Minecraft.getInstance().getMainRenderTarget(), tmp);
            //System.out.println("handle");
            //PostEffects.hsv_filter.process(tmp, Minecraft.getInstance().getMainRenderTarget(), 1, 0.97f);
        }
    }

    @Override
    public SE_Pipeline getPipeline() {
        //System.out.println("Get");
        return ppl;
    }
}
