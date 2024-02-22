package com.guhao.init;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class Tabs {
    public static CreativeModeTab TAB_GU_HAO_SAMA;

    public static void load() {
        TAB_GU_HAO_SAMA = new CreativeModeTab("gu_hao_sama") {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(Items.ENDER_EYE);
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return false;
            }
        };
    }
}