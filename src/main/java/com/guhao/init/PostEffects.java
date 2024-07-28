package com.guhao.init;

import com.guhao.client.particle.screeneffects.shaderpasses.HsvFilter;
import com.guhao.client.particle.screeneffects.shaderpasses.PostPassBase;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class PostEffects {
    public static PostPassBase blit;
    public static HsvFilter hsvFilterEffect;
    public static void register(RegisterShadersEvent event){
        try {
            System.out.println("Load Shader");
            ResourceManager rm = Minecraft.getInstance().getResourceManager();
            blit = new PostPassBase("guhao:blit",rm);
            hsvFilterEffect = new HsvFilter("guhao:hsv_filter",rm);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
