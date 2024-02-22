package com.guhao.renderers;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public abstract class ParticleSwordRenderer<T extends Entity> extends EntityRenderer<T> {
    protected ParticleSwordRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }
}