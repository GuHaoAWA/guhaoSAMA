package com.guhao;


import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.guhao.api.GuHaoSpecialAttackAnimation;
import com.guhao.init.ParticleType;
import com.guhao.utils.BattleUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import reascer.wom.animation.attacks.SpecialAttackAnimation;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.SourceTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.guhao.GuHaoColliderPreset.BIG_ATTACK;
import static com.guhao.Guhao.MODID;


public class GuHaoAnimations {


    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(MODID, GuHaoAnimations::register);
    }

    public static StaticAnimation NB_ATTACK;
    public static StaticAnimation ENDER;
    public static StaticAnimation PASSIVE;
    public static StaticAnimation V_GUHAO_SHEATHING_AUTO1;
    public static StaticAnimation GUHAO_BATTOJUTSU_DASH;
    public static StaticAnimation GUHAO_UCHIGATANA_SHEATHING_AUTO;
    public static StaticAnimation GUHAO_UCHIGATANA_SHEATHING_DASH;
    public static StaticAnimation GUHAO_UCHIGATANA_SHEATH_AIR_SLASH;
    public static StaticAnimation GUHAO_DASH;
    public static StaticAnimation SACRIFICE;
    public static StaticAnimation BIU;
    public static StaticAnimation GUHAO_BIU;
    public static StaticAnimation BLOOD_JUDGEMENT;
    public static StaticAnimation BLOOD_BURST;

    private static void register() {
        HumanoidArmature biped = Armatures.BIPED;
        Random rand = new Random();
        float a = 1 + rand.nextFloat(0.25f, 0.85f);
        float b = 2 + rand.nextFloat(0.25f, 0.85f);
        float c = 3 + rand.nextFloat(0.05f, 0.45f);
        SACRIFICE = (new ActionAnimation(0.01F, 1.851F, "biped/sacrifice", biped)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0f, 10.0f, 5, biped.toolR, InteractionHand.MAIN_HAND)))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, SC)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.415F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.sacrifice(ep), AnimationEvent.Side.SERVER),
                        //AnimationEvent.TimeStampedEvent.create(0.605F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.sacrifice_post(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.805F, Animations.ReusableSources.FRACTURE_GROUND_SIMPLE, AnimationEvent.Side.SERVER).params(new Vec3f(0.0F, -0.32F, 0.0F), Armatures.BIPED.rootJoint, 3.0, 0.45F)}));
        BLOOD_BURST = (new ActionAnimation(0.01F, 1.851F, "biped/blood_burst", biped)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                //.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, BLOOD_BURST_S)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.305F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_burst(ep), AnimationEvent.Side.SERVER),
                }));
        NB_ATTACK = (new AirSlashAnimation(0.01F, 0.15F, 0.386F, 0.55F, BIG_ATTACK, biped.rootJoint, "biped/very_gaint_big_cool_attack", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(c))
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.46F, Animations.ReusableSources.FRACTURE_GROUND_SIMPLE, AnimationEvent.Side.CLIENT).params(new Vec3f(0.0F, -0.24F, 0f), Armatures.BIPED.toolR, 4.25, 0.45F))
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.12f, 0.50f, 20, biped.toolR, InteractionHand.MAIN_HAND))));
        GUHAO_DASH = (new DashAttackAnimation(0.1F, 0.09F, 0.1F, 0.545F, 0.625F, null, biped.toolR, "biped/guhao_dash", biped))
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.25F)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(b))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.multiplier(2.0f))
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD)
                .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, true)
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.1f, 0.65f, 6, biped.toolR, InteractionHand.MAIN_HAND)));
        ENDER = (new ActionAnimation(0.01F, 0.485F, "biped/ender", biped))
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.381F, (livingEntityPatch, staticAnimation, objects) -> BattleUtils.Guhao_Battle_utils.ender(livingEntityPatch), AnimationEvent.TimeStampedEvent.Side.SERVER)});
        PASSIVE = new StaticAnimation(0.05F, false, "biped/passive", biped)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.15F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.SWORD_IN));
        V_GUHAO_SHEATHING_AUTO1 = (new BasicAttackAnimation(0.01F, 0.16F, 0.45F, 0.785F, null, biped.toolR, "biped/auto1", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(b))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3.0F)))
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.16f, 10.0f, 6, biped.toolR, InteractionHand.MAIN_HAND)));
        GUHAO_BATTOJUTSU_DASH =
                new AttackAnimation(0.15F, 0.43F, 0.7F, 0.92F, 1.4F, GuHaoColliderPreset.GUHAO_BATTOJUTSU_DASH, biped.rootJoint, "biped/battojutsu_dash", biped)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                        .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD)
                        .addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                        .addEvents(
                                AnimationEvent.TimeStampedEvent.create(0.05F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.SWORD_IN),
                                AnimationEvent.TimeStampedEvent.create(0.7F, (entitypatch, animation, params) -> {
                                    Entity entity = entitypatch.getOriginal();
                                    entity.level.addParticle(ParticleType.ENTITY_AFTER_IMG_BLOOD.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
                                    Random random = entitypatch.getOriginal().getRandom();
                                    double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * 2.0D;
                                    double y = entity.getY();
                                    double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * 2.0D;
                                    entity.level.addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005D, 0.0D, 0.0D);
                                }, AnimationEvent.Side.CLIENT),
                                AnimationEvent.TimeStampedEvent.create(0.8F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_blade(ep), AnimationEvent.Side.SERVER)
                        )
                        .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.5f, 1.15f, 16, biped.toolR, InteractionHand.MAIN_HAND)));
        GUHAO_UCHIGATANA_SHEATHING_AUTO = new BasicAttackAnimation(0.05F, 0.0F, 0.06F, 0.65F, null, biped.rootJoint, "biped/uchigatana_sheath_auto", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.055F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_blade(ep), AnimationEvent.Side.SERVER),
                })
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0f, 10.0f, 6, biped.toolR, InteractionHand.MAIN_HAND)));
        GUHAO_UCHIGATANA_SHEATHING_DASH = new DashAttackAnimation(0.05F, 0.05F, 0.2F, 0.35F, 0.65F, null, biped.rootJoint, "biped/uchigatana_sheath_dash", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.75F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.345F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_blade(ep), AnimationEvent.Side.SERVER),
                })
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0f, 10.0f, 6, biped.toolL, InteractionHand.MAIN_HAND)));
        GUHAO_UCHIGATANA_SHEATH_AIR_SLASH = (new AirSlashAnimation(0.1F, 0.1F, 0.16F, 0.3F, null, biped.toolR, "biped/uchigatana_sheath_airslash", biped))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP)
                .addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, 2.0F)
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.155F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_blade(ep), AnimationEvent.Side.SERVER),})
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.1f, 0.175f, 6, biped.toolR, InteractionHand.MAIN_HAND)));
        BIU = (new BasicAttackAnimation(0.01F, 0.315F, 0.385F, 1.05F, GuHaoColliderPreset.ENDER_LASER, biped.toolR, "biped/biu", biped))
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.195F, (entitypatch, self, params) -> {
                    if (entitypatch instanceof PlayerPatch) {
                        entitypatch.getOriginal().level.playSound((Player) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BUZZ, SoundSource.PLAYERS, 0.6F, 1.5F);
                    }

                }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.345F, (entitypatch, self, params) -> {
                    if (entitypatch instanceof PlayerPatch) {
                        entitypatch.getOriginal().level.playSound((Player) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.7F, 1.0F);
                    }

                    OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(1.0F), Armatures.BIPED.toolR);
                    OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(1.0F), Armatures.BIPED.toolR);
                    transformMatrix.translate(new Vec3f(0.0F, -0.6F, -0.3F));
                    transformMatrix2.translate(new Vec3f(0.0F, -0.8F, -0.3F));
                    OpenMatrix4f CORRECTION = (new OpenMatrix4f()).rotate(-((float) Math.toRadians(entitypatch.getOriginal().yRotO + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F));
                    OpenMatrix4f.mul(CORRECTION, transformMatrix, transformMatrix);
                    OpenMatrix4f.mul(CORRECTION, transformMatrix2, transformMatrix2);
                    int n = 40;
                    double r = 0.2;
                    double t = 0.01;

                    for (int i = 0; i < n; ++i) {
                        double theta = 6.283185307179586 * (new Random()).nextDouble();
                        double phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
                        double x = r * Math.cos(phi) * Math.cos(theta);
                        double y = r * Math.cos(phi) * Math.sin(theta);
                        double z = r * Math.sin(phi);
                        Vec3f direction = new Vec3f((float) x, (float) y, (float) z);
                        OpenMatrix4f rotation = (new OpenMatrix4f()).rotate(-((float) Math.toRadians((double) ((LivingEntity) entitypatch.getOriginal()).yBodyRotO)), new Vec3f(0.0F, 1.0F, 0.0F));
                        rotation.rotate((transformMatrix.m11 + 0.07F) * 1.5F, new Vec3f(1.0F, 0.0F, 0.0F));
                        OpenMatrix4f.transform3v(rotation, direction, direction);
                        entitypatch.getOriginal().level.addParticle(ParticleTypes.FLAME, (double) transformMatrix.m30 + ((LivingEntity) entitypatch.getOriginal()).getX(), (double) transformMatrix.m31 + ((LivingEntity) entitypatch.getOriginal()).getEyeY()-1.12, (double) transformMatrix.m32 + ((LivingEntity) entitypatch.getOriginal()).getZ(), (double) (transformMatrix2.m30 - transformMatrix.m30 + direction.x), (double) (transformMatrix2.m31 - transformMatrix.m31 + direction.y), (double) (transformMatrix2.m32 - transformMatrix.m32 + direction.z));
                    }

                    HitResult ray = entitypatch.getOriginal().pick(10.0, 0.7F, false);
                    entitypatch.getOriginal().level.addParticle(EpicFightParticles.LASER.get(), (double) transformMatrix.m30 + ((LivingEntity) entitypatch.getOriginal()).getX(), (double) transformMatrix.m31 + ((LivingEntity) entitypatch.getOriginal()).getEyeY()-1.12, (double) transformMatrix.m32 + ((LivingEntity) entitypatch.getOriginal()).getZ(), ray.getLocation().x, ray.getLocation().y, ray.getLocation().z);
                }, AnimationEvent.Side.CLIENT));
        GUHAO_BIU = (new BasicAttackAnimation(0.01F, 0.315F, 0.385F, 1.05F, GuHaoColliderPreset.ENDER_LASER, biped.toolR, "biped/guhao_biu", biped))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(1.5F))
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, SoundEvents.BLAZE_SHOOT)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundEvents.FIREWORK_ROCKET_BLAST)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.195F, (entitypatch, self, params) -> {
                    if (entitypatch instanceof PlayerPatch) {
                        entitypatch.getOriginal().level.playSound((Player) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.BUZZ, SoundSource.PLAYERS, 0.6F, 1.5F);
                    }

                }, AnimationEvent.Side.CLIENT), AnimationEvent.TimeStampedEvent.create(0.345F, (entitypatch, self, params) -> {
                    if (entitypatch instanceof PlayerPatch) {
                        entitypatch.getOriginal().level.playSound((Player) entitypatch.getOriginal(), entitypatch.getOriginal(), EpicFightSounds.LASER_BLAST, SoundSource.PLAYERS, 0.7F, 1.0F);
                    }

                    OpenMatrix4f transformMatrix = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(1.0F), Armatures.BIPED.toolR);
                    OpenMatrix4f transformMatrix2 = entitypatch.getArmature().getBindedTransformFor(entitypatch.getArmature().getPose(1.0F), Armatures.BIPED.toolR);
                    transformMatrix.translate(new Vec3f(0.0F, -0.6F, -0.3F));
                    transformMatrix2.translate(new Vec3f(0.0F, -0.8F, -0.3F));
                    OpenMatrix4f CORRECTION = (new OpenMatrix4f()).rotate(-((float) Math.toRadians(entitypatch.getOriginal().yRotO + 180.0F)), new Vec3f(0.0F, 1.0F, 0.0F));
                    OpenMatrix4f.mul(CORRECTION, transformMatrix, transformMatrix);
                    OpenMatrix4f.mul(CORRECTION, transformMatrix2, transformMatrix2);
                    int n = 40;
                    double r = 0.2;
                    double t = 0.01;

                    for (int i = 0; i < n; ++i) {
                        double theta = 6.283185307179586 * (new Random()).nextDouble();
                        double phi = ((new Random()).nextDouble() - 0.5) * Math.PI * t / r;
                        double x = r * Math.cos(phi) * Math.cos(theta);
                        double y = r * Math.cos(phi) * Math.sin(theta);
                        double z = r * Math.sin(phi);
                        Vec3f direction = new Vec3f((float) x, (float) y, (float) z);
                        OpenMatrix4f rotation = (new OpenMatrix4f()).rotate(-((float) Math.toRadians(entitypatch.getOriginal().yBodyRotO)), new Vec3f(0.0F, 1.0F, 0.0F));
                        rotation.rotate((transformMatrix.m11 + 0.07F) * 1.5F, new Vec3f(1.0F, 0.0F, 0.0F));
                        OpenMatrix4f.transform3v(rotation, direction, direction);
                        entitypatch.getOriginal().level.addParticle(ParticleType.BLOOD_FIRE_FLAME.get(), (double) transformMatrix.m30 + entitypatch.getOriginal().getX(), (double) transformMatrix.m31 + ((LivingEntity) entitypatch.getOriginal()).getEyeY()-1.12, (double) transformMatrix.m32 + ((LivingEntity) entitypatch.getOriginal()).getZ(), (double) (transformMatrix2.m30 - transformMatrix.m30 + direction.x), transformMatrix2.m31 - transformMatrix.m31 + direction.y, (double) (transformMatrix2.m32 - transformMatrix.m32 + direction.z));
                    }

                    HitResult ray = entitypatch.getOriginal().pick(10.0, 0.7F, false);
                    entitypatch.getOriginal().level.addParticle(ParticleType.GUHAO_LASER.get(), (double) transformMatrix.m30 + entitypatch.getOriginal().getX(), (double) transformMatrix.m31 + entitypatch.getOriginal().getEyeY()-1.12, (double) transformMatrix.m32 + ((LivingEntity) entitypatch.getOriginal()).getZ(), ray.getLocation().x, ray.getLocation().y, ray.getLocation().z);
                }, AnimationEvent.Side.CLIENT));

        BLOOD_JUDGEMENT = (new SpecialAttackAnimation(0.01F, "biped/blood_judgement", biped,
                new SpecialAttackAnimation.Phase(0.0F, 0.3F, 0.52F, Float.MAX_VALUE, 1.20f, biped.toolR, GuHaoColliderPreset.SACRIFICE_ATTACK),
                new SpecialAttackAnimation.Phase(1.2000001f, 2.95F, 3.154F, Float.MAX_VALUE, Float.MAX_VALUE, biped.rootJoint, GuHaoColliderPreset.GUHAO_BATTOJUTSU_DASH))
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(100.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.56F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3.6F))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(SourceTags.WEAPON_INNATE))
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
                .addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, 0.0F)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, false)
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.RESET_PLAYER_COMBO_COUNTER, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.7F, 1.32F))
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.295F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.SWORD_IN),
                        AnimationEvent.TimeStampedEvent.create(0.412F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_sound_1(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(0.744F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_post(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.745F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p2(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.795F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p2(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.845F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p2(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.895F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p2(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.945F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p2(ep), AnimationEvent.Side.CLIENT),
                        AnimationEvent.TimeStampedEvent.create(0.955F, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.SWORD_IN),
                        AnimationEvent.TimeStampedEvent.create(1.00F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_effect(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(1.185F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_sound_2(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(1.20F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_cut(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(1.2001F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p1(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(1.45F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_cut(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(1.48F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_sound_2(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(1.70F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_cut(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.20F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_cut(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.21F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_sound_2(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.46F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_cut(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.71F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_cut(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.945F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_sound_3(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.946F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_blade(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.9465F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_needles(ep), AnimationEvent.Side.SERVER),
                        AnimationEvent.TimeStampedEvent.create(2.948F, (ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_p2(ep), AnimationEvent.Side.CLIENT),
                })
                .addEvents(AnimationProperty.StaticAnimationProperty.TIME_PERIOD_EVENTS, new AnimationEvent.TimePeriodEvent[] {
                        AnimationEvent.TimePeriodEvent.create(2.94F,3.149999999999999999F,(ep, anim, objs) -> BattleUtils.Guhao_Battle_utils.blood_judgement_hurt(ep), AnimationEvent.Side.SERVER)
                })
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, BLOOD_JUDGEMENT_SPEED)
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.194F, 3.2F, 20, biped.toolR, InteractionHand.MAIN_HAND)))
        );
    }


    public static final AnimationProperty.PlaybackTimeModifier NB = (self, entitypatch, speed, elapsedTime) -> 1.25F;
    public static final AnimationProperty.PlaybackTimeModifier SC = (self, entitypatch, speed, elapsedTime) -> 0.666F;
    public static final AnimationProperty.PlaybackTimeModifier BLOOD_BURST_S = (self, entitypatch, speed, elapsedTime) -> 0.75F;
    public static final AnimationProperty.PlaybackTimeModifier BLOOD_JUDGEMENT_SPEED = (self, entitypatch, speed, elapsedTime) -> 1.0F;

    public static List<TrailInfo> newTFL(TrailInfo... tfs) {
        return Lists.newArrayList(tfs);
    }

    public static TrailInfo newTF(float start, float end, int lifetime, Joint joint, InteractionHand hand) {
        JsonObject je = new JsonObject();
        je.addProperty("joint", joint.getName());
        je.addProperty("start_time", start);
        je.addProperty("end_time", end);
        je.addProperty("item_skin_hand", hand.toString());
        je.addProperty("lifetime", lifetime);
        return TrailInfo.deserialize(je);
    }
}
