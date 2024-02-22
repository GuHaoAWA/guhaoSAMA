package com.guhao.mixins;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.client.particle.TrailParticle;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;

@Mixin(value = TrailParticle.class, remap = false, priority = 5000)
public abstract class TrailMixin extends TextureSheetParticle {

    List<Vec3> finalStartPositions;
    List<Vec3> finalEndPositions;
    @Mutable
    @Final
    @Shadow
    private final Joint joint;
    @Mutable
    @Final
    @Shadow
    private final TrailInfo trailInfo;
    @Mutable
    @Final
    @Shadow
    private final StaticAnimation animation;
    @Mutable
    @Final
    @Shadow
    private final LivingEntityPatch<?> entitypatch;
    @Shadow
    private boolean animationEnd;
    @Shadow
    private float startEdgeCorrection = 0.0F;
    private TrailMixin(ClientLevel level, LivingEntityPatch<?> entitypatch, Joint joint, StaticAnimation animation, TrailInfo trailInfo, SpriteSet spriteSet) {
        super(level, 0, 0, 0);
        this.joint = joint;
        this.entitypatch = entitypatch;
        this.animation = animation;
        this.hasPhysics = false;
        this.trailInfo = trailInfo;
        Vec3 entityPos = entitypatch.getOriginal().position();
        float size = (float) Math.max(this.trailInfo.start.length(), this.trailInfo.end.length()) * 2.0F;
        this.setSize(size, size);
        this.move(entityPos.x, entityPos.y + entitypatch.getOriginal().getEyeHeight(), entityPos.z);
        this.setSpriteFromAge(spriteSet);
    }
/*
    @Redirect(method = "tick",at = @At(value= "INVOKE", target = "Lyesman/epicfight/api/utils/math/CubicBezierCurve;getBezierInterpolatedPoints(Ljava/util/List;III)Ljava/util/List;"))
    public List<Vec3> finalStartPositions(List<Vec3> idx, int t, int j, int start) {
        this.finalStartPositions = idx;
        this.finalEndPositions = idx;
        return idx;
    }
*/
    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
            if (entitypatch.getOriginal().getOffhandItem().getItem() == Items.NETHERITE_SWORD) {
                for (int i = 0; i < finalStartPositions.size(); i++) {
                    Vec3 startPos = finalStartPositions.get(i);
                    Vec3 endPos = finalEndPositions.get(i);
                    // 计算粒子的数量和间隔
                    int particleCount = 1; // 粒子数量
                    double interval = 0.1; // 粒子之间的间隔
                    // 计算粒子的方向和速度
                    Vec3 direction = endPos.subtract(startPos).normalize();
                    double speed = 0.25; // 粒子的速度
                    for (int j = 0; j < particleCount; j++) {
                        Vec3 particlePos = startPos.add(direction.scale(j * interval));
                        level.addParticle(ParticleTypes.SOUL, true, particlePos.x, particlePos.y, particlePos.z, speed * direction.x, speed * direction.y, speed * direction.z);
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, (entitypatch.getOriginal().getMainHandItem())) >= 1) {
                            // 创建岩浆粒子，并设置位置和速度
                            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, true, particlePos.x, particlePos.y, particlePos.z, speed * direction.x, speed * direction.y, speed * direction.z);
                        }
                    }
                }
            }
        }

    }