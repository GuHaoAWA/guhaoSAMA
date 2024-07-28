package com.guhao.item.model;

import com.guhao.init.Effect;
import com.guhao.item.GUHAO;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class GUHAOModel extends AnimatedGeoModel<GUHAO> {
	@Override
	public ResourceLocation getAnimationFileLocation(GUHAO animatable) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && mc.player.hasEffect(Effect.GUHAO.get())) return new ResourceLocation("guhao", "animations/bloodslashingblade_ex.animation.json");
		else return new ResourceLocation("guhao", "animations/bloodslashingblade.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GUHAO animatable) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && mc.player.hasEffect(Effect.GUHAO.get())) return new ResourceLocation("guhao", "geo/bloodslashingblade_ex-magic.geo.json");
		else return new ResourceLocation("guhao", "geo/bloodslashingblade.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GUHAO animatable) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player != null && mc.player.hasEffect(Effect.GUHAO.get())) return new ResourceLocation("guhao", "textures/items/bloodslashingblade_ex.png");
		else return new ResourceLocation("guhao", "textures/items/bloodslashingblade.png");
	}
}
