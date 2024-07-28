package com.guhao.mixins;

import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LivingEvent.class,remap = false)
public abstract class LivingEventMixin {


}
