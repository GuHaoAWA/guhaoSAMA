package com.guhao.events;

import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.ArrayList;

public class DoppelgangerTickEvent {
    public static void execute(Entity entity, Level level) {
        entity.setSprinting(true);
        if (entity instanceof Mob mob) {
            AdvancedCustomHumanoidMobPatch<?> ep = EpicFightCapabilities.getEntityPatch(mob, AdvancedCustomHumanoidMobPatch.class);
            for (Entity entityiterator : new ArrayList<>(level.players())) {
                mob.getNavigation().moveTo(entityiterator.getX()+1, entityiterator.getY(), entityiterator.getZ(), 1.2);
                PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(entityiterator, PlayerPatch.class);
               if (ep != null && pp != null && pp.getAnimator().getPlayerFor(null).getAnimation() instanceof ActionAnimation sa &&
                       (!(ep.getAnimator().getPlayerFor(null).getAnimation() instanceof ActionAnimation))) {
                   StaticAnimation staticAnimation = sa;
                   ep.getAnimator().playAnimationInstantly(staticAnimation);
               }
            }
        }
    }
}
