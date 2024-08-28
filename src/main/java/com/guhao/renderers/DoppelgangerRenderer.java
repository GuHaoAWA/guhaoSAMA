package com.guhao.renderers;

import com.guhao.entity.DoppelgangerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DoppelgangerRenderer extends HumanoidMobRenderer<DoppelgangerEntity, HumanoidModel<DoppelgangerEntity>> {
    public DoppelgangerRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DoppelgangerEntity entity) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            return mc.player.getSkinTextureLocation();
        } else return new ResourceLocation("guhao:textures/entities/doppelganger.png");

    }
}
