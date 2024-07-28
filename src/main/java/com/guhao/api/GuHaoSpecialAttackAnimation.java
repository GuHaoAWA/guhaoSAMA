package com.guhao.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.animation.property.JointMask;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GuHaoSpecialAttackAnimation extends GuHaoBasicMultipleAttackAnimation {
    public GuHaoSpecialAttackAnimation(float convertTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, antic, antic, contact, recovery, collider, colliderJoint, path, armature);
    }

    public GuHaoSpecialAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, path, armature, new AttackAnimation.Phase(0.0F, antic, preDelay, contact, recovery, Float.MAX_VALUE, colliderJoint, collider));
    }

    public GuHaoSpecialAttackAnimation(float convertTime, float antic, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, path, armature, new AttackAnimation.Phase(0.0F, antic, antic, contact, recovery, Float.MAX_VALUE, hand, colliderJoint, collider));
    }

    public GuHaoSpecialAttackAnimation(float convertTime, String path, Armature armature, boolean Coordsetter, AttackAnimation.Phase... phases) {
        super(convertTime, path, armature, phases);
    }

    public GuHaoSpecialAttackAnimation(float convertTime, String path, Armature armature, AttackAnimation.Phase... phases) {
        super(convertTime, path, armature, phases);
        this.newTimePair(0.0F, Float.MAX_VALUE);
        this.addStateRemoveOld(EntityState.TURNING_LOCKED, false);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_LOC_TARGET);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
            LivingEntity attackTarget = entitypatch.getTarget();
            if (!(Boolean)self.getRealAnimation().getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
                TransformSheet transform = ((TransformSheet)self.getTransfroms().get("Root")).copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 0;
                int endFrame = transform.getKeyframes().length - 1;
                Vec3f keyLast = keyframes[endFrame].transform().translation();
                Vec3 pos = ((LivingEntity)entitypatch.getOriginal()).getEyePosition();
                Vec3 targetpos = attackTarget.position();
                float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance() * 1.75F - (attackTarget.getBbWidth() + ((LivingEntity)entitypatch.getOriginal()).getBbWidth()) * 0.75F, 0.0F);
                Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
                float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);

                for(int i = startFrame; i <= endFrame; ++i) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                }

                transformSheet.readFrom(transform);
            } else {
                transformSheet.readFrom((TransformSheet)self.getTransfroms().get("Root"));
            }

        });
    }

    protected void onLoaded() {
        super.onLoaded();
        if (!this.properties.containsKey(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED)) {
            float basisSpeed = Float.parseFloat(String.format(Locale.US, "%.2f", 1.0F / this.totalTime));
            this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, basisSpeed);
        }

    }

    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);
        boolean stiffAttack = ((GameRules.BooleanValue)((LivingEntity)entitypatch.getOriginal()).level.getGameRules().getRule(EpicFightGamerules.STIFF_COMBO_ATTACKS)).get();
        if (!isEnd && !nextAnimation.isMainFrameAnimation() && entitypatch.isLogicalClient() && !stiffAttack) {
            float playbackSpeed = 0.05F * this.getPlaySpeed(entitypatch);
            entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
        }

    }

    public TypeFlexibleHashMap<EntityState.StateFactor<?>> getStatesMap(LivingEntityPatch<?> entitypatch, float time) {
        TypeFlexibleHashMap<EntityState.StateFactor<?>> stateMap = super.getStatesMap(entitypatch, time);
        if (!((GameRules.BooleanValue)((LivingEntity)entitypatch.getOriginal()).level.getGameRules().getRule(EpicFightGamerules.STIFF_COMBO_ATTACKS)).get()) {
            stateMap.put(EntityState.MOVEMENT_LOCKED, Optional.of(false));
        }

        return stateMap;
    }

    public Vec3 getCoordVector(LivingEntityPatch<?> entitypatch, DynamicAnimation dynamicAnimation) {
        Vec3 vec3 = super.getCoordVector(entitypatch, dynamicAnimation);
        if (entitypatch.shouldBlockMoving() && (Boolean)this.getProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE).orElse(false)) {
            vec3 = vec3.scale(0.0);
        }

        return vec3;
    }

    public boolean isJointEnabled(LivingEntityPatch<?> entitypatch, Layer.Priority layer, String joint) {
        if (layer == Layer.Priority.HIGHEST) {
            return !JointMaskEntry.BASIC_ATTACK_MASK.isMasked(entitypatch.getCurrentLivingMotion(), joint);
        } else {
            return super.isJointEnabled(entitypatch, layer, joint);
        }
    }

    public JointMask.BindModifier getBindModifier(LivingEntityPatch<?> entitypatch, Layer.Priority layer, String joint) {
        if (layer == Layer.Priority.HIGHEST) {
            List<JointMask> list = JointMaskEntry.BIPED_UPPER_JOINTS_WITH_ROOT;
            int position = list.indexOf(JointMask.of(joint));
            return position >= 0 ? ((JointMask)list.get(position)).getBindModifier() : null;
        } else {
            return super.getBindModifier(entitypatch, layer, joint);
        }
    }

    public boolean isBasicAttackAnimation() {
        return false;
    }

    public float applyAntiStunLock(Entity hitten, float anti_stunlock, EpicFightDamageSource source, AttackAnimation.Phase phase, String tag, String replaceTag) {
        boolean isPhaseFromSameAnimnation = false;
        String phaseID;
        int i;
        String var10000;
        if (hitten.level.getBlockState(new BlockPos(new Vec3(hitten.getX(), hitten.getY() - 1.0, hitten.getZ()))).isAir() && source.getStunType() != StunType.FALL) {
            var10000 = String.valueOf(this.getId());
            phaseID = var10000 + "-" + String.valueOf(phase.contact);
            if (tag.split(":").length > 3) {
                if (String.valueOf(this.getId()).equals(tag.split(":")[3].split("-")[0]) && !String.valueOf(phase.contact).equals(tag.split(":")[3].split("-")[1])) {
                    anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.98F;
                    isPhaseFromSameAnimnation = true;
                } else {
                    anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.95F;
                    isPhaseFromSameAnimnation = false;
                }
            }

            for(i = 3; i < tag.split(":").length && i < 5; ++i) {
                if (tag.split(":")[i].equals(phaseID)) {
                    anti_stunlock *= 0.6F;
                }
            }
        } else {
            var10000 = String.valueOf(this.getId());
            phaseID = var10000 + "-" + String.valueOf(phase.contact);
            if (tag.split(":").length > 3) {
                if (String.valueOf(this.getId()).equals(tag.split(":")[3].split("-")[0]) && !String.valueOf(phase.contact).equals(tag.split(":")[3].split("-")[1])) {
                    anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.98F;
                    isPhaseFromSameAnimnation = true;
                } else {
                    anti_stunlock = Float.valueOf(tag.split(":")[1]) * 0.8F;
                    isPhaseFromSameAnimnation = false;
                }
            }

            for(i = 3; i < tag.split(":").length && i < 7; ++i) {
                if (tag.split(":")[i].equals(phaseID)) {
                    anti_stunlock *= 0.6F;
                }
            }
        }

        hitten.removeTag(tag);
        int maxSavedAttack;
        if (isPhaseFromSameAnimnation) {
            replaceTag = "anti_stunlock:" + anti_stunlock + ":" + hitten.tickCount;
            maxSavedAttack = 6;
        } else {
            replaceTag = "anti_stunlock:" + anti_stunlock + ":" + hitten.tickCount + ":" + this.getId() + "-" + phase.contact;
            maxSavedAttack = 5;
        }

        for(i = 3; i < tag.split(":").length && i < maxSavedAttack; ++i) {
            replaceTag = replaceTag.concat(":" + tag.split(":")[i]);
        }

        hitten.addTag(replaceTag);
        return anti_stunlock;
    }
}
