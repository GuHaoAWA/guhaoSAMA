package com.guhao.events;

import com.guhao.GuHaoAnimations;
import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Effect;
import com.guhao.star.regirster.Sounds;
import com.guhao.star.units.Guard_Array;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.types.GuardAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PlayerTickEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            execute(event, event.player);
        }
    }

    public static void execute(Player p) {
        execute(null, p);
    }

    private static void execute(@Nullable Event event, Player entity) {
        if (entity == null)
            return;
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
        if (pp != null && !(pp.getAnimator().getPlayerFor(null).getAnimation() == GuHaoAnimations.SETTLEMENT | pp.getAnimator().getPlayerFor(null).getAnimation() == GuHaoAnimations.JIANQIE)) {
            pp.getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, UUID.fromString("f6f8c8d8-6e54-4b02-8f18-7c6f3e6e3f7f"));
            pp.getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, UUID.fromString("f6f8c8d8-6e54-4b02-8f18-7c6f3e6e3f6f"));
        }
    }
}
