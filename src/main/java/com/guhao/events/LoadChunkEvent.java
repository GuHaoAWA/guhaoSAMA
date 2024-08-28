package com.guhao.events;

import com.guhao.GuHaoAnimations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import javax.annotation.Nullable;

import static com.guhao.Guhao.MODID;

@Mod.EventBusSubscriber
public class LoadChunkEvent {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        execute(event, event.getEntityLiving());
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        LivingEntityPatch<?> ep = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
        if (ep != null && ep.getAnimator().getPlayerFor(null).getAnimation() == GuHaoAnimations.BIG_STAR && entity.getLevel() instanceof ServerLevel serverLevel) {
            ForgeChunkManager.forceChunk(serverLevel,MODID,entity, (int) entity.getX(),(int) entity.getZ(),true,true);

            if (entity instanceof Mob mob) {
                mob.setPersistenceRequired();
            }
        }
    }

}
