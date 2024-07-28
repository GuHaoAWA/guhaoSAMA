package com.guhao.mixins;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import yesman.epicfight.world.entity.eventlistener.EventTrigger;
import yesman.epicfight.world.entity.eventlistener.PlayerEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Map;
import java.util.UUID;

@Mixin(value = PlayerEventListener.class,remap = false)
public class PlayerEventListenerMixin {
    @Shadow
    private Map<PlayerEventListener.EventType<? extends PlayerEvent<?>>, TreeMultimap<Integer, EventTrigger<? extends PlayerEvent<?>>>> events;
    /**
     * @author
     * @reason
     */
    @Overwrite
    public <T extends PlayerEvent<?>> void removeListener(PlayerEventListener.EventType<T> eventType, UUID uuid, int priority) {
        Multimap<Integer, EventTrigger<? extends PlayerEvent<?>>> map = this.events.get(eventType);
        if (map == null) {
            return;
        }

        priority = Math.max(priority, -1);
        map.get(priority).removeIf((trigger) -> trigger != null && uuid != null && trigger.is(uuid));
    }
}
