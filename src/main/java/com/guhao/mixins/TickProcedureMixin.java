package com.guhao.mixins;

import com.guhao.init.Items;
import com.guhao.init.ParticleType;
import net.m3tte.tactical_imbuements.TacticalImbuementsMod;
import net.m3tte.tactical_imbuements.init.TacticalImbuementsModParticleTypes;
import net.m3tte.tactical_imbuements.procedures.TickProcedure;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.LinkedList;
import java.util.Random;

@Mixin(value = TickProcedure.class,remap = false)
public class TickProcedureMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void doArmatureParticle(Joint j, LivingEntityPatch<?> entityPatch, SimpleParticleType particle, float amount, Vec3 particleSpeed, LinkedList<Vec3> offsets) {
        Random r = new Random();
        if (entityPatch.getOriginal().getMainHandItem().getItem() == Items.GUHAO.get() && particle == TacticalImbuementsModParticleTypes.FLAME_PARTICLE.get()) {

            if (r.nextDouble() < 0.3) {
                particle = ParticleType.BLOOD_FIRE_FLAME2.get();
            } else {
                particle = ParticleType.BLOOD_FIRE_FLAME.get();
            }
        }
        Pose currentPose = entityPatch.getArmature().getCurrentPose();
        Level l = entityPatch.getOriginal().level;
        for (float i = 1; i <= 9; i += 2f) {
            Pose middlePose = entityPatch.getArmature().getPose((i + r.nextInt(3) - 1) / 10);
            Vec3 posMid = entityPatch.getOriginal().getPosition((i + r.nextInt(3) - 1) / 10);

            OpenMatrix4f middleModelTf = OpenMatrix4f.createTranslation((float)posMid.x, (float)posMid.y, (float)posMid.z)
                    .mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS)
                            .mulBack(entityPatch.getModelMatrix((float) (i + r.nextInt(3) - 1) / 10F)));

            OpenMatrix4f middleJointTf = entityPatch.getArmature().getBindedTransformFor(middlePose, j).mulFront(middleModelTf);


            for (Vec3 modifier : offsets) {
                if (r.nextInt((int)(100 / amount)) < 40) {

                    if (entityPatch.getOriginal().getMainHandItem().getItem() == Items.GUHAO.get()) {
                        modifier = modifier.add(r.nextFloat() * 0.2f - 0.1, r.nextFloat() * 0.2f - 0.1, r.nextFloat() * 2.1f - 2.0);
                    } else {
                        modifier = modifier.add(r.nextFloat() * 0.2f - 0.1, r.nextFloat() * 0.2f - 0.1, r.nextFloat() * 0.2f - 0.1);
                    }
                    Vec3 particlePos = OpenMatrix4f.transform(middleJointTf, modifier);
                    l.addParticle(particle, particlePos.x, particlePos.y, particlePos.z, particleSpeed.x, particleSpeed.y, particleSpeed.z);
                }

            }
        }
    }}
