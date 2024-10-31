//package com.guhao.renderers;
//
//import net.minecraft.client.renderer.entity.ThrownItemRenderer;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.EntityRenderersEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import static com.guhao.init.Entities.WAVE;
//
//
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//public class SwordQiRenderers {
//    @SubscribeEvent
//    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
//        event.registerEntityRenderer(WAVE.get(), ThrownItemRenderer::new);
//    }
//}
