package com.guhao.client.particle.screeneffects.shaderpasses;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class Blit extends PostPassBase {
    public Blit(ResourceManager rsmgr) throws IOException {
        super(new EffectInstance(rsmgr, "guhao:blit"));
    }

}
