package com.guhao;


import com.guhao.utils.BattleUtils;
import net.minecraft.world.InteractionHand;
import reascer.wom.animation.attacks.SpecialAttackAnimation;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.forgeevent.AnimationRegistryEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Random;

import static com.dfdyz.epicacg.registry.MyAnimations.newTF;
import static com.dfdyz.epicacg.registry.MyAnimations.newTFL;
import static com.guhao.GuHaoColliderPreset.BIG_ATTACK;
import static com.guhao.Guhao.MODID;


public class GuHaoAnimations {

    public static void registerAnimations(AnimationRegistryEvent event) {
        event.getRegistryMap().put(MODID, GuHaoAnimations::E);
    }

    public static StaticAnimation NB_ATTACK;
    public static StaticAnimation ENDER;
    public static StaticAnimation PASSIVE;
    public static StaticAnimation GUHAO_SHEATHING_AUTO1;

    private static void E() {
        HumanoidArmature biped = Armatures.BIPED;
        Random rand = new Random();
        float r = rand.nextFloat(70, 85);
        NB_ATTACK = (new SpecialAttackAnimation(0.01F, "biped/very_gaint_big_cool_attack", biped, new AttackAnimation.Phase(0.0F, 0.16F, 1.36F, 2F, 2F, biped.rootJoint, BIG_ATTACK)))
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.BLADE_RUSH_SKILL)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_RUSH_FINISHER)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.KNOCKDOWN)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.setter(r))
                .addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, true)
                .addEvents(AnimationEvent.TimeStampedEvent.create(0.4F, Animations.ReusableSources.FRACTURE_GROUND_SIMPLE, AnimationEvent.Side.CLIENT).params(new Vec3f(0.0F, -0.24F, -10.0F), Armatures.BIPED.toolR, 7.55, 7.55F))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, NB)
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0f, 10.0f, 20, biped.toolR, InteractionHand.MAIN_HAND)));
        ENDER = (new ActionAnimation(0.0666F, 0.0000001F, "biped/ender", biped))
                .addProperty(AnimationProperty.StaticAnimationProperty.TIME_STAMPED_EVENTS, new AnimationEvent.TimeStampedEvent[]{
                        AnimationEvent.TimeStampedEvent.create(0.000001F, (livingEntityPatch, staticAnimation, objects) -> BattleUtils.HL.ender(livingEntityPatch), AnimationEvent.TimeStampedEvent.Side.SERVER)});
        PASSIVE = new StaticAnimation(0.01F, false, "biped/passive", biped);
        GUHAO_SHEATHING_AUTO1 = (new BasicAttackAnimation(0.01F, 0.16F, 0.45F, 0.785F, (Collider) null, biped.toolR, "biped/auto1", biped)
                .addProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, ValueModifier.adder(30.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(2.0F))
                .addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(3.0F)))
                .addProperty(ClientAnimationProperties.TRAIL_EFFECT, newTFL(newTF(0.16f, 10.0f, 6, biped.toolR, InteractionHand.MAIN_HAND)));
    }

    public static final AnimationProperty.PlaybackTimeModifier NB = (self, entitypatch, speed, elapsedTime) -> 1.25F;
    public static final AnimationProperty.PlaybackTimeModifier auto = (self, entitypatch, speed, elapsedTime) -> 1.0F;
}
