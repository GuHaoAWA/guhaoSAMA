package com.guhao.events;

import com.guhao.init.Effect;
import com.guhao.init.Items;
import com.guhao.init.ParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RingEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
//        Player p = event.player;
//        Level l = p.getLevel();
//        if (p.getMainHandItem().getItem() == Items.GUHAO.get() && p.hasEffect(Effect.GUHAO.get())) {
//            l.addParticle(ParticleType.RING.get(), p.getX(), p.getY() + 0.05, p.getZ(), 0, 0, 0);
//        }
    }
}
