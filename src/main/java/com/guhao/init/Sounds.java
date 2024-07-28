package com.guhao.init;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(
        modid = "guhao",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class Sounds {

    public static final HashSet<SoundEvent> SOUND_EVENTS = Sets.newHashSet();
    public static final SoundEvent LAUGH = RegSound("laugh");
    public static final SoundEvent DAO1 = RegSound("dao1");
    public static final SoundEvent DAO2 = RegSound("dao2");
    public static final SoundEvent DAO3 = RegSound("dao3");
    public static final SoundEvent BIU = RegSound("biu");
    public static final SoundEvent BLOOD = RegSound("blood");

    public Sounds() {
    }

    private static SoundEvent RegSound(String name) {
        ResourceLocation r = new ResourceLocation("guhao", name);
        SoundEvent s = (new SoundEvent(r)).setRegistryName(name);
        SOUND_EVENTS.add(s);
        return s;
    }

    @SubscribeEvent
    public static void onSoundRegistry(RegistryEvent.Register<SoundEvent> event) {
        SOUND_EVENTS.forEach((s) -> event.getRegistry().register(s));
    }
}

