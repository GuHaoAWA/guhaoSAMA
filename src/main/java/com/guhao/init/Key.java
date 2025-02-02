package com.guhao.init;

import com.guhao.Guhao;
import com.guhao.network.BloodBurstMessage;
import com.guhao.network.EnderMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class Key {
    public static final KeyMapping ENDER = new KeyMapping("key.guhao.ender", GLFW.GLFW_KEY_KP_ADD, "key.categories.guhao");
    public static final KeyMapping BLOOD_BURST = new KeyMapping("key.guhao.blood_burst", GLFW.GLFW_KEY_KP_SUBTRACT, "key.categories.guhao");
    public static final KeyMapping CTRL = new KeyMapping("key.guhao.ctrl", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.guhao");
    public static final KeyMapping RIGHT = new KeyMapping("key.guhao.shift", GLFW.GLFW_MOUSE_BUTTON_2, "key.categories.guhao");


    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(ENDER);
        ClientRegistry.registerKeyBinding(BLOOD_BURST);
        ClientRegistry.registerKeyBinding(CTRL);
        ClientRegistry.registerKeyBinding(RIGHT);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (Minecraft.getInstance().screen == null) {
                if (event.getKey() == BLOOD_BURST.getKey().getValue()) {
                    if (event.getAction() == GLFW.GLFW_PRESS) {
                        Guhao.PACKET_HANDLER.sendToServer(new BloodBurstMessage(0, 0));
                        BloodBurstMessage.pressAction(Minecraft.getInstance().player, 0, 0);
                    }
                }
                if (event.getKey() == ENDER.getKey().getValue()) {
                    if (event.getAction() == GLFW.GLFW_PRESS) {
                        Guhao.PACKET_HANDLER.sendToServer(new EnderMessage(0, 0));
                        EnderMessage.pressAction(Minecraft.getInstance().player, 0, 0);
                    }
                }
            }
        }
    }
}
