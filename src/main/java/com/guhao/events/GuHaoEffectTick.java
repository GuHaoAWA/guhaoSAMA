package com.guhao.events;

import com.guhao.client.particle.screeneffects.HsvFilterEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GuHaoEffectTick {
    public static void execute(LivingEntity entity) {
            Vec3 pos = entity.position();
            HsvFilterEffect effect = new HsvFilterEffect(pos);
            effect.lifetime = 20;
            ScreenEffectEngine.PushScreenEffect(effect);
        }
    }

