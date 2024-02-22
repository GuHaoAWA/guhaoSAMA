package com.guhao.item.model;

import com.guhao.item.GUHAO;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class GUHAOModel extends AnimatedGeoModel<GUHAO> {
	@Override
	public ResourceLocation getAnimationFileLocation(GUHAO animatable) {
		return new ResourceLocation("guhao", "animations/bloodslashingblade.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GUHAO animatable) {
		return new ResourceLocation("guhao", "geo/bloodslashingblade.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GUHAO animatable) {
		return new ResourceLocation("guhao", "textures/items/bloodslashingblade.png");
	}
}
