package com.guhao.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class DoppelgangerSpawnEvent {
    public static void execute(Entity entity, Level level) {
        if (entity == null)
            return;
        if (entity instanceof LivingEntity _entity) {
            {
                final Vec3 _center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
                List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate((-1) / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof Player player) {
                        ItemStack main_setstack = (entity instanceof LivingEntity ? player.getMainHandItem() : new ItemStack(Blocks.AIR));
                        ItemStack off_setstack = (entity instanceof LivingEntity ? player.getOffhandItem() : new ItemStack(Blocks.AIR));
                        main_setstack.setCount(player.getMainHandItem().getCount());
                        off_setstack.setCount(player.getOffhandItem().getCount());
                        _entity.setItemInHand(InteractionHand.MAIN_HAND, main_setstack);
                        _entity.setItemInHand(InteractionHand.OFF_HAND, off_setstack);
                    }
                }
            }
        }
    }
}
