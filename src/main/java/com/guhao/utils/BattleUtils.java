package com.guhao.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class BattleUtils {
    public BattleUtils() {
    }
    public static class HL {
        public static void ender(LivingEntityPatch<?> livingEntityPatch) {
            float speed = 2.0f;
            Entity _shootFrom = livingEntityPatch.getOriginal();
            Level projectileLevel = _shootFrom.level;
            Projectile _entityToSpawn = new Object() {
                public Projectile getProjectile() {
                    Level level = livingEntityPatch.getOriginal().level;
                    Entity shooter = livingEntityPatch.getOriginal();
                    Projectile entityToSpawn = new ThrownEnderpearl(EntityType.ENDER_PEARL, level);
                    entityToSpawn.setOwner(shooter);
                    return entityToSpawn;
                }
            }.getProjectile();
            _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.095, _shootFrom.getZ());
            _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, speed, 0);
            projectileLevel.addFreshEntity(_entityToSpawn);
        }
    }
}