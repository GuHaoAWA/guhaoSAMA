package com.guhao.mixins;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BloodSlashProjectile.class,remap = false)
public class BloodSlashProjectileMixin extends Projectile implements AntiMagicSusceptible {
    protected BloodSlashProjectileMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    @Shadow
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(BloodSlashProjectile.class, EntityDataSerializers.FLOAT);
    @Override

    public void onAntiMagic(PlayerMagicData playerMagicData) {
        this.discard();
    }



    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 0.5F);
    }

//    /**
//     * @author
//     * @reason
//     */
//    @Overwrite
//    public void shoot(Vec3 rotation) {
//        this.setDeltaMovement(rotation.scale(1.0));
//        setYRot(45);
//        setXRot(45);
//    }

}
