package com.guhao;

import com.dfdyz.epicacg.network.Netmgr;
import com.guhao.capability.GuHaoCapability;
import com.guhao.init.*;
import com.guhao.skills.GuHaoSkills;
import com.guhao.skills.SacrificeSkill;
import com.guhao.star.efmex.IntegrationHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
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
    public static final Logger LOGGER = LogUtils.getLogger();
    public Guhao() {
        if (ModList.get().isLoaded("annoying_villagersbychentu") | ModList.get().isLoaded("annoying_villagers")) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                System.out.print("Stop playing annoying villagers,IDIOT");
            }
            System.exit(0);
        }
        //Tabs.load();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        GuHaoSkills.registerSkills();
        Entities.REGISTRY.register(bus);
        bus.addListener(this::setupClient);
//        bus.addListener(PostEffects::register);
        bus.addListener(GuHaoCapability::register);
        bus.addListener(GuHaoAnimations::registerAnimations);
//        bus.addListener(ParticleType::RP);
        bus.addListener(SacrificeSkill::register);
        Effect.REGISTRY.register(bus);
        ParticleType.PARTICLES.register(bus);
        Items.REGISTRY.register(bus);
        GeckoLib.initialize();
//        bus.register(this);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }
    private void setupClient(final FMLClientSetupEvent event){
        GuHaoAnimations.LoadCamAnims();
    }
    @SubscribeEvent
    public static void modConstruction(FMLConstructModEvent event) {
        IntegrationHandler.construct();
    }
    @SubscribeEvent
    public void setupCommon(FMLCommonSetupEvent event) {
        event.enqueueWork(Netmgr::register);
        GuHaoSkills.registerSkills();
    }
}

