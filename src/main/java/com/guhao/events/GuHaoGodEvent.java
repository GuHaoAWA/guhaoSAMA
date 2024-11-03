package com.guhao.events;

import com.guhao.init.Items;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GuHaoGodEvent {
    @SubscribeEvent
    public static void onPlayerTick(ServerChatEvent event) {
        execute(event, event.getPlayer().level, event.getMessage(), event.getPlayer());
    }

    public static void execute(Level world, String message, Player player) {
        execute(null, world, message, player);
    }

    private static void execute(@Nullable Event event, Level world, String message, Player player) {
        if (message.equals("-666-")) {
            if (player.getUUID().toString().equals("69a66a4c-0b7e-31c1-ac2a-8c852ed4c1c5") | player.getName().getString().equals("GuHao_")) {
                ItemStack _setstack = new ItemStack(Items.GUHAO.get());
                _setstack.setCount(1);
                ItemHandlerHelper.giveItemToPlayer(player, _setstack);
                if (!world.isClientSide() && player.getServer() != null)
                    player.getLevel().getServer().getPlayerList().broadcastMessage(new TextComponent("§4孤豪_大人，您终于来了！！！"), ChatType.SYSTEM, Util.NIL_UUID);
            } else {
                player.displayClientMessage(new TextComponent("§4你不是孤豪_，滚"), false);
            }
        }
    }
}
