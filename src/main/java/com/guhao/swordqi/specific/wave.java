package com.guhao.swordqi.specific;

import com.guhao.init.Entities;
import com.guhao.swordqi.swordqi_ph2;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class wave extends swordqi_ph2 implements ItemSupplier{
    public wave(PlayMessages.SpawnEntity packet, Level world) {
        super(Entities.WAVE.get(), world);
    }

    public wave(EntityType<? extends wave> type, Level world) {
        super(type, world);
    }

    public wave(EntityType<? extends wave> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public wave(EntityType<? extends wave> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;

    }


    @Override
    public void tick() {
        super.tick();
        AABB bounds = this.getBoundingBox(); {
            this.minX = (float) (this.getX() - 0.5f);
            this.minY = (float) (this.getY() - 0.5f);
            this.minZ = (float) (this.getZ() - 0.5f);
            this.maxX = (float) (this.getX() + 0.5f);
            this.maxY = (float) (this.getY() + 0.5f);
            this.maxZ = (float) (this.getZ() + 0.5f);
            new AABB(minX, minY, minZ, maxX, maxY, maxZ);
        };

        // 获取碰撞箱内的所有实体
        List<Entity> entities = this.level.getEntities(null, bounds);

        // 遍历碰撞到的实体，并对其造成伤害
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.hurt(DamageSource.thrown(this, this.getOwner()), 2.0F);
            }
        }
    }

    public static wave shoot(Level world, LivingEntity entity, Random random, float power, double damage, int knockback) {
        wave entityarrow = new wave(Entities.WAVE.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return entityarrow;
    }

    public static wave shoot(LivingEntity entity, LivingEntity target) {
        wave entityarrow = new wave(Entities.WAVE.get(), entity, entity.level);
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(5);
        entityarrow.setKnockback(5);
        entityarrow.setCritArrow(false);
        entity.level.addFreshEntity(entityarrow);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (new Random().nextFloat() * 0.5f + 1));
        return entityarrow;
    }


    @Override
    public ItemStack getItem() {
        return new ItemStack(Blocks.LAVA);
    }
}
