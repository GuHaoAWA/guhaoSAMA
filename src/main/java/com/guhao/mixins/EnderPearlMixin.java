package com.guhao.mixins;

import com.guhao.init.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.gameasset.EpicFightSounds;

@Mixin(value = ThrownEnderpearl.class, priority = 5000)
public abstract class EnderPearlMixin extends ThrowableItemProjectile {
    public EnderPearlMixin(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }
    @Inject(method = "onHit", at = @At("HEAD"))
    protected void REonHit(HitResult p_37504_, CallbackInfo ci) {
        Entity entity = this.getOwner();
        super.onHit(p_37504_);
        assert entity != null;
        entity.playSound(EpicFightSounds.ENDER_DRAGON_BREATH_FINALE, 0f, 0f);
        if (entity instanceof LivingEntity E && E.getMainHandItem().getItem() == Items.GUHAO.get()) {
            for (int i = 0; i < 32; ++i) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
            }
            if (!this.level.isClientSide && !this.isRemoved()) {
                if (entity instanceof ServerPlayer serverplayer) {
                    if (serverplayer.connection.getConnection().isConnected() && serverplayer.level == this.level && !serverplayer.isSleeping()) {
                        if (entity.isPassenger()) {
                            serverplayer.dismountTo(this.getX(), this.getY(), this.getZ());
                        } else {
                            entity.teleportTo(this.getX(), this.getY(), this.getZ());
                        }
                        entity.teleportTo(this.getX(), this.getY(), this.getZ());

                        entity.resetFallDistance();
                    } //Forge: End
                }
            } else {
                entity.teleportTo(this.getX(), this.getY(), this.getZ());
                entity.resetFallDistance();
            }
            this.discard();
        }
    }
}

