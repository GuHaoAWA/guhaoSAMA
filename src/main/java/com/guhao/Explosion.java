package com.guhao;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Random;

public class Explosion {

    public static void createExplosion(Entity player, double x, double y, double z, int radius) {
        Level world = player.getLevel();
        Random random = new Random();

        for (double i = x - radius; i < x + radius; i++) {
            for (double j = y - radius; j < y + radius; j++) {
                for (double k = z - radius; k < z + radius; k++) {
                    double distance = Math.sqrt((player.getX() - i) * (player.getX() - i) + (player.getY() - j) * (player.getY() - j) + (player.getZ() - k) * (player.getZ() - k));
                    double randomRadius = random.nextDouble() * radius;

                    if (distance <= randomRadius) {
                        BlockPos pos = new BlockPos(i, j, k);
                        Block block = world.getBlockState(pos).getBlock();
                        if (block != Blocks.AIR) {
                            float damage = ((float) (1 - distance / radius)*10f);
                            block.onBlockExploded(world.getBlockState(pos), world, pos, new net.minecraft.world.level.Explosion(world,player,pos.getX(),pos.getY(),pos.getZ(),5));
                            if (random.nextDouble() < 0.2) {
                                world.setBlock(pos, Blocks.FIRE.defaultBlockState(),1);
                            }
                        }
                    }
                }
            }
        }
    }
}