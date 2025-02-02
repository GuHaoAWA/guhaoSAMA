package com.guhao.events;

import com.guhao.GuHaoAnimations;
import com.guhao.client.particle.par.ONEJCBladeTrailWide;
import com.guhao.init.Effect;
import com.guhao.init.ParticleType;
import com.guhao.init.Sounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.github.alexthe666.alexsmobs.effect.AMEffectRegistry.EXSANGUINATION;


public class HitEvent {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, LivingEntity player) {
        if (entity == null)
            return;
        if (entity instanceof LivingEntity livingEntity) {
            if (!livingEntity.hasEffect(new MobEffectInstance(EXSANGUINATION).getEffect())) livingEntity.addEffect(new MobEffectInstance(new MobEffectInstance(EXSANGUINATION).getEffect(), 200, 1, false, true));
            else livingEntity.addEffect(new MobEffectInstance(new MobEffectInstance(EXSANGUINATION).getEffect(), 200, livingEntity.getEffect(EXSANGUINATION).getAmplifier() + 1, false, true));
        }
        if (world instanceof ServerLevel _level) _level.sendParticles(ParticleType.TWO_EYE.get(), x, y+0.5, z, 1, 0.5, 0.5, 0.5, 0);
        Player player1 = (Player) player;
        PlayerPatch<?> pp = EpicFightCapabilities.getEntityPatch(player1, PlayerPatch.class);
        DynamicAnimation animation = pp.getAnimator().getPlayerFor(null).getAnimation();
        new Object() {
            private int ticks = 0;
            private float waitTicks;
            private LevelAccessor world;

            public void start(LevelAccessor world, int waitTicks) {
                this.waitTicks = waitTicks;
                MinecraftForge.EVENT_BUS.register(this);
                this.world = world;
            }

            @SubscribeEvent
            public void tick(TickEvent.ServerTickEvent event) {
                if (event.phase == TickEvent.Phase.END) {
                    this.ticks += 1;
                    if (this.ticks >= this.waitTicks)
                        run();
                }
            }

            private void run() {
                if (animation == GuHaoAnimations.SETTLEMENT) {
                    pp.playSound(Sounds.DAO3,1f,1f);
                    Random random = new Random();
                    Vec3 papos;
                    Vec3 pos = new Vec3(x, y + 1, z);
                    boolean add = random.nextBoolean();
                    double offsetX = add ? random.nextDouble(5.0, 10.0) : -random.nextDouble(5.0, 10.0);
                    double offsetZ = add ? random.nextDouble(5.0, 10.0) : -random.nextDouble(5.0, 10.0);
                    papos = new Vec3(x + offsetX, y + 1, z + offsetZ);
                    Vec3 velocity = pos.subtract(papos).normalize();
                    if (world instanceof ServerLevel serverLevel) {
                        ParticleOptions particle = ParticleType.ONE_JC_BLOOD_JUDGEMENT_WIDE.get();
                        serverLevel.sendParticles(particle, x, y, z, 10, velocity.x, velocity.y, velocity.z, 100.0);
                    }
                    LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
                    if (entitypatch != null) entitypatch.applyStun(StunType.LONG, 5.0f);
                    entity.hurt(DamageSource.playerAttack(player1).bypassArmor().damageHelmet().bypassInvul(), random.nextFloat(20.0F, 30.0F));
                }
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }.start(world, 6);
        
        if (player1.hasEffect(Effect.GUHAO.get())) {
            new Object() {
                private int ticks = 0;
                private float waitTicks;
                private LevelAccessor world;

                public void start(LevelAccessor world, int waitTicks) {
                    this.waitTicks = waitTicks;
                    MinecraftForge.EVENT_BUS.register(this);
                    this.world = world;
                }

                @SubscribeEvent
                public void tick(TickEvent.ServerTickEvent event) {
                    if (event.phase == TickEvent.Phase.END) {
                        this.ticks += 1;
                        if (this.ticks >= this.waitTicks)
                            run();
                    }
                }

                private void run() {
                    {
                        final Vec3 _center = new Vec3((player.getX()), (player.getY()), (player.getZ()));
                        List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8.2d / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                                .toList();
                        for (Entity entityiterator : _entfound) {
                            if (!(entityiterator instanceof Player)) {
                                if (world instanceof ServerLevel _level) {
                                    _level.sendParticles(ParticleType.TWO_EYE.get(), entityiterator.getX(), entityiterator.getY()+1, entityiterator.getZ(), 1, 0.5, 0.5, 0.5, 0);
                                }
                                player1.setHealth(player1.getHealth() + 1.0F);
                                if (entityiterator != entity) entityiterator.hurt(DamageSource.playerAttack(player1).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 4.5F);

                            }
                        }
                    }
                    new Object() {
                        private int ticks = 0;
                        private float waitTicks;
                        private LevelAccessor world;

                        public void start(LevelAccessor world, int waitTicks) {
                            this.waitTicks = waitTicks;
                            MinecraftForge.EVENT_BUS.register(this);
                            this.world = world;
                        }

                        @SubscribeEvent
                        public void tick(TickEvent.ServerTickEvent event) {
                            if (event.phase == TickEvent.Phase.END) {
                                this.ticks += 1;
                                if (this.ticks >= this.waitTicks)
                                    run();
                            }
                        }

                        private void run() {
                            entity.hurt(DamageSource.playerAttack(player1).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 7.5F);
                            if (world instanceof ServerLevel _level) {
                                Random r = new Random();
                                _level.sendParticles(ParticleType.ONE_JC_BLOOD_JUDGEMENT.get(), x, y, z, 1, 0.15, 0, 0.15, 0);
                                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(Sounds.BIU.getLocation()), SoundSource.PLAYERS, r.nextFloat(0.75f,1.0f), r.nextFloat(0.75f,1.25f));

                            }
                            new Object() {
                                private int ticks = 0;
                                private float waitTicks;
                                private LevelAccessor world;

                                public void start(LevelAccessor world, int waitTicks) {
                                    this.waitTicks = waitTicks;
                                    MinecraftForge.EVENT_BUS.register(this);
                                    this.world = world;
                                }

                                @SubscribeEvent
                                public void tick(TickEvent.ServerTickEvent event) {
                                    if (event.phase == TickEvent.Phase.END) {
                                        this.ticks += 1;
                                        if (this.ticks >= this.waitTicks)
                                            run();
                                    }
                                }

                                private void run() {
                                    if (world instanceof ServerLevel _level) {
                                        Random r = new Random();
                                        _level.sendParticles(ParticleType.ONE_JC_BLOOD_JUDGEMENT.get(), x, y, z, 1, 0.15, 0, 0.15, 0);
                                        _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(Sounds.BIU.getLocation()), SoundSource.PLAYERS, r.nextFloat(0.75f,1.0f), r.nextFloat(0.75f,1.25f));
                                    }
                                    entity.hurt(DamageSource.playerAttack(player1).setMagic().bypassArmor().damageHelmet().bypassInvul().bypassMagic(), 7.5F);
                                    MinecraftForge.EVENT_BUS.unregister(this);
                                }
                            }.start(world, 10);
                            MinecraftForge.EVENT_BUS.unregister(this);
                        }
                    }.start(world, 14);
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }.start(world, 1);
        }
    }
}

/*
                    double diagonalLength = Math.sqrt(_center.distanceToSqr(_center) * 3); // 计算正方体的对角线长度
                    double radius = diagonalLength / 2; // 内切球的半径
                    List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(radius), e -> true)
                            .stream()
                            .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
                            .toList();
                    _level.sendParticles((SimpleParticleType) (ParticleType.EYE.get()), (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 1, 0.5, 1, 0.5, 0);
                                                       _level.sendParticles((SimpleParticleType) (ParticleType.EYE.get()), z, y, z, 1, 0.5, 1, 0.5, 0);
                                                        entityiterator.hurt(DamageSource.playerAttack(player1), 10);
 */