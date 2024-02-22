package com.guhao.events;

import com.guhao.init.Items;
import com.guhao.init.ParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RingEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player p = event.player;
        Level l = p.getLevel();
        if (p.getMainHandItem().getItem() == Items.GUHAO.get()) {
            l.addParticle(ParticleType.RING.get(), p.getX(), p.getY() + 0.1, p.getZ(), 0, 0, 0);
        }
    }
}
