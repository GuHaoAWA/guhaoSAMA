package com.guhao.mixins;

import com.guhao.init.Items;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;


@Mixin(value = GuardSkill.class, priority = 10000, remap = false)
public abstract class GuardGuHaoMixin extends Skill {
    public GuardGuHaoMixin(Builder builder) {
        super(builder);
    }

    @Final
    @Shadow
    protected static SkillDataManager.SkillDataKey<Float> PENALTY;

    @Shadow
    protected abstract boolean isBlockableSource(DamageSource damageSource, boolean advanced);

    @Shadow
    protected abstract float getPenalizer(CapabilityItem itemCapability);

    @Shadow
    protected abstract StaticAnimation getGuardMotion(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, GuardSkill.BlockType blockType);

    @Shadow
    public abstract void dealEvent(PlayerPatch<?> playerpatch, HurtEvent.Pre event, boolean advanced);


    @Inject(at = @At("HEAD"), method = "guard")
    public void mixin_guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced, CallbackInfo ci) {
        if (event.getPlayerPatch().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get()) {
            DamageSource damageSource = (DamageSource) event.getDamageSource();
            if (isBlockableSource(damageSource, advanced)) {
                ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.CLASH, -0.06F, 0.12F);
                ServerPlayer serveerPlayer = event.getPlayerPatch().getOriginal();
                EpicFightParticles.EVISCERATE.get().spawnParticleWithArgument(serveerPlayer.getLevel(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, serveerPlayer, damageSource.getDirectEntity());
                Entity var10 = damageSource.getDirectEntity();
                if (var10 instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) var10;
                    knockback += (float) EnchantmentHelper.getKnockbackBonus(livingEntity) * 0.1F;
                }

                float penalty = 0.0f;
                float consumeAmount = 0.0f;
                ((ServerPlayerPatch) event.getPlayerPatch()).knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                ((ServerPlayerPatch) event.getPlayerPatch()).consumeStaminaAlways(consumeAmount);
                container.getDataManager().setDataSync(PENALTY, penalty, (ServerPlayer) ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal());
                GuardSkill.BlockType blockType = ((ServerPlayerPatch) event.getPlayerPatch()).hasStamina(0.0F) ? GuardSkill.BlockType.GUARD : GuardSkill.BlockType.GUARD_BREAK;
                StaticAnimation animation = getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                if (animation != null) {
                    ((ServerPlayerPatch) event.getPlayerPatch()).playAnimationSynchronized(animation, 0.0F);
                }

                if (blockType == GuardSkill.BlockType.GUARD_BREAK) {
                    ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                }

                dealEvent(event.getPlayerPatch(), event, advanced);
            }

        } else {
            DamageSource damageSource = (DamageSource) event.getDamageSource();
            if (this.isBlockableSource(damageSource, advanced)) {
                ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.CLASH, -0.05F, 0.1F);
                ServerPlayer serveerPlayer = (ServerPlayer) ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal();
                ((HitParticleType) EpicFightParticles.HIT_BLUNT.get()).spawnParticleWithArgument(serveerPlayer.getLevel(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, serveerPlayer, damageSource.getDirectEntity());
                Entity var10 = damageSource.getDirectEntity();
                if (var10 instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) var10;
                    knockback += (float) EnchantmentHelper.getKnockbackBonus(livingEntity) * 0.1F;
                }

                float penalty = (Float) container.getDataManager().getDataValue(PENALTY) + this.getPenalizer(itemCapability);
                float consumeAmount = penalty * impact;
                ((ServerPlayerPatch) event.getPlayerPatch()).knockBackEntity(damageSource.getDirectEntity().position(), knockback);
                ((ServerPlayerPatch) event.getPlayerPatch()).consumeStaminaAlways(consumeAmount);
                container.getDataManager().setDataSync(PENALTY, penalty, (ServerPlayer) ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal());
                GuardSkill.BlockType blockType = ((ServerPlayerPatch) event.getPlayerPatch()).hasStamina(0.0F) ? GuardSkill.BlockType.GUARD : GuardSkill.BlockType.GUARD_BREAK;
                StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
                if (animation != null) {
                    ((ServerPlayerPatch) event.getPlayerPatch()).playAnimationSynchronized(animation, 0.0F);
                }

                if (blockType == GuardSkill.BlockType.GUARD_BREAK) {
                    ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.NEUTRALIZE_MOBS, 3.0F, 0.0F, 0.1F);
                }

                this.dealEvent(event.getPlayerPatch(), event, advanced);
            }

        }
    }
}
