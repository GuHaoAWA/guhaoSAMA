package com.guhao.events;

import com.google.common.collect.Sets;
import com.guhao.client.particle.screeneffects.ScreenEffectBase;
import com.guhao.client.particle.screeneffects.shaderpasses.PostEffectPipelines;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

import static com.guhao.Guhao.MODID;
import static net.minecraftforge.client.event.RenderLevelStageEvent.Stage.AFTER_PARTICLES;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ScreenEffectEngine {
    static Set<ScreenEffectBase> effects = Sets.newConcurrentHashSet();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void OnRender(RenderLevelStageEvent event){
        if(event.getStage() == AFTER_PARTICLES){
            effects.forEach((e) -> {
                if(e.shouldPost(event.getCamera(), event.getFrustum()))
                    PostEffectPipelines.PostEffectQueue.add(e.getPipeline());
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getInstance().isPaused()) return;

        if(event.phase == TickEvent.Phase.END){
            effects.forEach(ScreenEffectBase::tick);
            effects.removeIf(ScreenEffectBase::shouldRemoved);
        }
    }


    public static void PushScreenEffect(ScreenEffectBase screenEffect){
        effects.removeIf(effects -> effects.ID.equals(screenEffect.ID));
        effects.add(screenEffect);
        //ystem.out.println("Add at: " + screenEffect.pos);
    }

}
