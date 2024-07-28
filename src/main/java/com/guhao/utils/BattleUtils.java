package com.guhao.utils;

import com.dfdyz.epicacg.client.screeneffect.ColorDispersionEffect;
import com.guhao.init.Effect;
import com.guhao.init.ParticleType;
import com.guhao.init.Sounds;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerMagicData;
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedle;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.github.alexthe666.alexsmobs.effect.AMEffectRegistry.EXSANGUINATION;

public class BattleUtils {
    public BattleUtils() {
    }
    public static class Guhao_Battle_utils {
        public static void ender(LivingEntityPatch<?> livingEntityPatch) {
            float speed = 2.5F;
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
            _entityToSpawn.setPos(_shootFrom.getX(), _shootFrom.getEyeY() - 0.09, _shootFrom.getZ());
            _entityToSpawn.shoot(_shootFrom.getLookAngle().x, _shootFrom.getLookAngle().y, _shootFrom.getLookAngle().z, speed, 0);
            projectileLevel.addFreshEntity(_entityToSpawn);
        }
        public static void blood_needles(LivingEntityPatch<?> ep) {
                ep.playSound(SoundRegistry.BLOOD_CAST.get(), 1.0F, 1.0F);
                Random random = new Random();
                Level world = ep.getOriginal().level;
                LivingEntity entity = ep.getOriginal();
                int count = 10;
                float damage = random.nextFloat(10f, 20f);
                int degreesPerNeedle = 360 / count;
                var raycast = Utils.raycastForEntity(world, entity, 32, true);
                for (int i = 0; i < count; i++) {
                    BloodNeedle needle = new BloodNeedle(world, entity);
                    int rotation = degreesPerNeedle * i - (degreesPerNeedle / 2);
                    needle.setDamage(damage);
                    needle.setZRot(rotation);
                    Vec3 spawn = entity.getEyePosition().add(new Vec3(0, 1.5, 0).zRot(rotation * Mth.DEG_TO_RAD).xRot(-entity.getXRot() * Mth.DEG_TO_RAD).yRot(-entity.getYRot() * Mth.DEG_TO_RAD));
                    needle.moveTo(spawn);
                    needle.shoot(raycast.getLocation().subtract(spawn).normalize());
                    world.addFreshEntity(needle);
                }
        }
        public static void blood_blade(LivingEntityPatch<?> ep) {
            ep.playSound(SoundRegistry.BLOOD_CAST.get(),1.0F,1.0F);
            Random random = new Random();
            Level world = ep.getOriginal().level;
            LivingEntity entity =ep.getOriginal();
            BloodSlashProjectile bloodSlash = new BloodSlashProjectile(world, entity);
            bloodSlash.setPos(entity.getEyePosition());
            bloodSlash.shoot(entity.getLookAngle());
            bloodSlash.setDamage(random.nextFloat(25f,45f));
            world.addFreshEntity(bloodSlash);
        }
        public static void sacrifice(LivingEntityPatch<?> livingEntityPatch){
            livingEntityPatch.playSound(Sounds.LAUGH,1.0F,1.0F);
            Level level = livingEntityPatch.getOriginal().getLevel();
            Vec3 position = livingEntityPatch.getOriginal().position();
            double x = position.x;
            double y = position.y;
            double z = position.z;
            LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(level);
                entityToSpawn.moveTo(Vec3.atBottomCenterOf(new BlockPos(x, y, z)));
                entityToSpawn.setVisualOnly(true);
                level.addFreshEntity(entityToSpawn);
            if (level instanceof ServerLevel _level) {
                _level.sendParticles(ParticleType.RED_RING.get(), x, y, z, 1, 0.1, 0.1, 0.1, 0);
                _level.sendParticles(ParticleType.RED_RING.get(), x, y+0.1, z, 1, 0.1, 0.1, 0.1, 0);
                _level.sendParticles(ParticleType.RED_RING.get(), x, y+0.2, z, 1, 0.1, 0.1, 0.1, 0);
            }
            level.addParticle(EpicFightParticles.EVISCERATE.get(),x,y+0.45,z,0,0,0);
            livingEntityPatch.getOriginal().clearFire();

            livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(Effect.GUHAO.get(), 1210, 0, false, true));
            livingEntityPatch.getOriginal().addEffect(new MobEffectInstance(EpicFightMobEffects.STUN_IMMUNITY.get(), 1210, 0, false, true));

            List<Entity> _entfound = level.getEntitiesOfClass(Entity.class, new AABB(position, position).inflate(50 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(position))).toList();
            for (Entity entityiterator : _entfound) {
                if ((entityiterator instanceof LivingEntity livingEntity) && (!(entityiterator instanceof Player))) {
                    if (livingEntity.getAttributes().hasAttribute(EpicFightAttributes.STAMINA_REGEN.get())) livingEntity.getAttribute(EpicFightAttributes.STAMINA_REGEN.get()).setBaseValue(livingEntity.getAttribute(EpicFightAttributes.STAMINA_REGEN.get()).getBaseValue() * 0.5);
                    if (livingEntity.getAttributes().hasAttribute(EpicFightAttributes.MAX_STAMINA.get())) livingEntity.getAttribute(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(livingEntity.getAttribute(EpicFightAttributes.MAX_STAMINA.get()).getBaseValue() * 0.5);
                    if (livingEntity.getAttributes().hasAttribute(livingEntity.getAttribute(Attributes.ARMOR).getAttribute())) livingEntity.getAttribute(Attributes.ARMOR).setBaseValue(0);
                    if (livingEntity.getAttributes().hasAttribute(Attributes.ARMOR_TOUGHNESS)) livingEntity.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
                    livingEntity.setTicksFrozen(100);
                    livingEntity.hurt(DamageSource.playerAttack(level.getNearestPlayer(livingEntityPatch.getOriginal(), -1)), 1);
                    LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(livingEntity, LivingEntityPatch.class);
                    if (entitypatch != null) {
                        entitypatch.playAnimationSynchronized(Animations.BIPED_KNOCKDOWN, 0.0F);
                    }
                    level.addParticle(ParticleType.TWO_EYE.get(), (livingEntity.getX()), (livingEntity.getY()+1), (livingEntity.getZ()), 0, 0, 0);
                }
            }

        }

        public static void blood_judgement_sound_1(LivingEntityPatch<?> ep) {
            ep.playSound(Sounds.DAO1,1.2F,1.2F);
        }
        public static void blood_judgement_sound_2(LivingEntityPatch<?> ep) {
            ep.playSound(Sounds.DAO2,1.2F,1.2F);
        }
        public static void blood_judgement_sound_3(LivingEntityPatch<?> ep) {
            ep.playSound(Sounds.DAO3,1.2F,1.2F);
        }
        public static void blood_judgement_p1(LivingEntityPatch<?> ep) {
            Vec3 vec3 = ep.getOriginal().position();
            double x = vec3.x;
            double y = vec3.y;
            double z = vec3.z;
            Level world = ep.getOriginal().level;
            if (world instanceof ServerLevel _level) {
                    _level.sendParticles(ParticleType.BLOOD_JUDGEMENT.get(), x + 5, (y + 8), z, 1, 0, 0, 0, 0);
                    _level.sendParticles(ParticleType.BLOOD_JUDGEMENT.get(), x - 5, (y + 8), z, 1, 0, 0, 0, 0);
                    _level.sendParticles(ParticleType.BLOOD_JUDGEMENT.get(), x, (y + 8), z + 5, 1, 0, 0, 0, 0);
                    _level.sendParticles(ParticleType.BLOOD_JUDGEMENT.get(), x, (y + 8), z - 5, 1, 0, 0, 0, 0);
            }
        }
        public static void blood_judgement_cut(LivingEntityPatch<?> ep) {
            Vec3 vec3 = ep.getOriginal().position();
            double x = vec3.x;
            double y = vec3.y;
            double z = vec3.z;
            Level world = ep.getOriginal().level;

            {
                final Vec3 _center = new Vec3((x + 4), y, z);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (!(world.getNearestPlayer(ep.getOriginal(),-1) == null)) entityiterator.hurt(DamageSource.playerAttack(world.getNearestPlayer(ep.getOriginal(),-1)).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 30);
                    entityiterator.setAirSupply(0);
                    LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                    if (entitypatch != null) {
                        entitypatch.cancelAnyAction();
                        entitypatch.applyStun(StunType.HOLD,1.5F);
                    }
                }
            }
            {
                final Vec3 _center = new Vec3((x - 4), y, z);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (!(world.getNearestPlayer(ep.getOriginal(),-1) == null)) entityiterator.hurt(DamageSource.playerAttack(world.getNearestPlayer(ep.getOriginal(), -1)).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 30);
                    entityiterator.setAirSupply(0);
                    LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                    if (entitypatch != null) {
                        entitypatch.cancelAnyAction();
                        entitypatch.applyStun(StunType.HOLD,1.5F);
                    }
                }
            }
            {
                final Vec3 _center = new Vec3(x, y, z+4);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (!(world.getNearestPlayer(ep.getOriginal(),-1) == null)) entityiterator.hurt(DamageSource.playerAttack(world.getNearestPlayer(ep.getOriginal(), -1)).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 30);
                    entityiterator.setAirSupply(0);
                    LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                    if (entitypatch != null) {
                        entitypatch.cancelAnyAction();
                        entitypatch.applyStun(StunType.HOLD,1.5F);
                    }
                }
            }
            {
                final Vec3 _center = new Vec3(x, y, z-4);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (!(world.getNearestPlayer(ep.getOriginal(),-1) == null)) entityiterator.hurt(DamageSource.playerAttack(world.getNearestPlayer(ep.getOriginal(), -1)).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 30);
                    entityiterator.setAirSupply(0);
                    LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                    if (entitypatch != null) {
                        entitypatch.cancelAnyAction();
                        entitypatch.applyStun(StunType.HOLD,1.5F);
                    }
                }
            }
        }

        public static void blood_judgement_p2(LivingEntityPatch<?> ep) {
            Entity entity = ep.getOriginal();
            entity.level.addParticle(ParticleType.ENTITY_AFTER_IMG_BLOOD.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0, 0);
        }

        public static void blood_judgement_post(LivingEntityPatch<?> ep) {
            Vec3 pos = Minecraft.getInstance().player.position();
            ColorDispersionEffect effect = new ColorDispersionEffect(pos);
            effect.lifetime = 58;
            //ScreenEffectEngine.PushScreenEffect(effect);
        }

        public static void blood_judgement_hurt(LivingEntityPatch<?> ep) {
            if (ep == null) {
                // 处理 ep 为 null 的情况
                return;
            }

            if (ep.isLogicalClient()) {
                // 处理 ep 是逻辑客户端的情况
                return;
            }

            Collection<?> currentlyAttackedEntities = ep.getCurrenltyAttackedEntities();
            if (currentlyAttackedEntities == null || currentlyAttackedEntities.isEmpty()) {
                // 处理 currentlyAttackedEntities 为 null 或空的情况
                return;
            }

            currentlyAttackedEntities.forEach((entity) -> {
                if (entity instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) entity;
                    if (le.equals(ep.getOriginal())) {
                        return;
                    }

                    LivingEntityPatch<?> lep = EpicFightCapabilities.getEntityPatch(le, LivingEntityPatch.class);
                    if (lep == null) {
                        return;
                    }

                    EpicFightParticles.EVISCERATE.get().spawnParticleWithArgument((ServerLevel) lep.getOriginal().getLevel(), HitParticleType.MIDDLE_OF_ENTITIES, HitParticleType.ZERO, lep.getOriginal(), lep.getOriginal());
                    lep.applyStun(StunType.KNOCKDOWN, 5.0F);
                }
            });
        }

        public static void blood_burst(LivingEntityPatch<?> ep) {
            Vec3 vec3 = ep.getOriginal().position();
            double x = vec3.x;
            double y = vec3.y;
            double z = vec3.z;
            Level world = ep.getOriginal().level;
            {
                final Vec3 _center = new Vec3(x, y, z);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(60 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof LivingEntity livingEntity && livingEntity.hasEffect(new MobEffectInstance(EXSANGUINATION).getEffect()) && !(livingEntity instanceof Player)) {
                        int amplifier = livingEntity.getEffect(new MobEffectInstance(EXSANGUINATION).getEffect()).getAmplifier();
                        if (world instanceof ServerLevel _level) {
                            Random r = new Random();
                            _level.playSound(null, new BlockPos(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(Sounds.BLOOD.getLocation()), SoundSource.PLAYERS, r.nextFloat(0.75f,1.0f), r.nextFloat(0.75f,1.25f));
                            _level.sendParticles(EpicFightParticles.EVISCERATE.get(), livingEntity.getX(), livingEntity.getY() + 1, livingEntity.getZ(), amplifier, 0.5, 0.5, 0.5, 0);
                        }
                        livingEntity.hurt(DamageSource.playerAttack(world.getNearestPlayer(ep.getOriginal(), -1)).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), amplifier*15.0F);
                        livingEntity.removeEffect(EXSANGUINATION);
                        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entityiterator, LivingEntityPatch.class);
                        if (entitypatch != null) {
                            entitypatch.applyStun(StunType.LONG, 3.0F);
                        }
                    }
                }
            }
        }

        public static void blood_judgement_effect(LivingEntityPatch<?> ep) {
            Vec3 vec3 = ep.getOriginal().position();
            double x = vec3.x;
            double y = vec3.y;
            double z = vec3.z;
            Level world = ep.getOriginal().level;
            {
                final Vec3 _center = new Vec3((x + 4), y, z);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(new MobEffectInstance(com.guhao.star.regirster.Effect.EXECUTED.get()).getEffect(), 42, 0, false, true));
                }
            }
            {
                final Vec3 _center = new Vec3((x - 4), y, z);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(new MobEffectInstance(com.guhao.star.regirster.Effect.EXECUTED.get()).getEffect(), 42, 0, false, true));
                }
            }
            {
                final Vec3 _center = new Vec3(x, y, z+4);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                    if (entityiterator instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(new MobEffectInstance(com.guhao.star.regirster.Effect.EXECUTED.get()).getEffect(), 42, 0, false, true));
                }
            }
            {
                final Vec3 _center = new Vec3(x, y, z-4);
                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
                for (Entity entityiterator : _entfound) {
                        if (entityiterator instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(new MobEffectInstance(com.guhao.star.regirster.Effect.EXECUTED.get()).getEffect(), 42, 0, false, true));
                    }
                }
            }
    }
}