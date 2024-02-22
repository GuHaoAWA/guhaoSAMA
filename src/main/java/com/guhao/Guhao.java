package com.guhao;

import com.guhao.capability.GuHaoCapability;
import com.guhao.events.RingEvent;
import com.guhao.init.Entities;
import com.guhao.init.Items;
import com.guhao.init.ParticleType;
import com.guhao.init.Tabs;
import com.guhao.skills.GuHaoSkills;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import software.bernie.geckolib3.GeckoLib;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod("guhao")
public class Guhao {
    public static final String MODID = "guhao";
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public Guhao() {
        if (ModList.get().isLoaded("annoying_villagersbychentu") | ModList.get().isLoaded("annoying_villagers")) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                System.out.print("Stop playing annoying villagers,IDIOT");
            }
            System.exit(0);
        }
        Tabs.load();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new RingEvent());
        GuHaoSkills.registerSkills();
        Entities.REGISTRY.register(bus);
        bus.addListener(GuHaoCapability::register);
        bus.addListener(GuHaoAnimations::registerAnimations);
        bus.addListener(ParticleType::RP);
        ParticleType.PARTICLES.register(bus);
        Items.REGISTRY.register(bus);
        GeckoLib.initialize();
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }
}

