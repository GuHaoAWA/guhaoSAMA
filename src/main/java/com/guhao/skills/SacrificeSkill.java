package com.guhao.skills;

import com.google.common.collect.Maps;
import com.guhao.GuHaoAnimations;
import com.guhao.init.Effect;
import com.guhao.init.Key;
import com.guhao.init.ParticleType;
import com.guhao.star.efmex.StarAnimations;
import com.guhao.star.regirster.Sounds;
import com.nameless.falchion.gameasset.FalchionAnimations;
import io.netty.buffer.Unpooled;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class SacrificeSkill extends WeaponInnateSkill {
    private final StaticAnimation[] animations;
    public final Map<StaticAnimation, AttackAnimation> comboAnimation = Maps.newHashMap();
    private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc49-b83e-16ae590db349");
    public static SkillDataManager.SkillDataKey<Boolean> IS_CTRL_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);

    public SacrificeSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
        this.animations = new StaticAnimation[]{FalchionAnimations.FALCHION_FORWARD, FalchionAnimations.FALCHION_BACKWARD, FalchionAnimations.FALCHION_SIDE};
//        this.comboAnimation.put(Animations.TACHI_AUTO3, (AttackAnimation) Animations.RUSHING_TEMPO3);
//        this.comboAnimation.put(Animations.UCHIGATANA_AUTO1, (AttackAnimation) Animations.RUSHING_TEMPO1);
//        this.comboAnimation.put(Animations.UCHIGATANA_AUTO3, (AttackAnimation) Animations.RUSHING_TEMPO2);
//        this.comboAnimation.put(Animations.LONGSWORD_AUTO2, (AttackAnimation) StarAnimations.KATANA_SHEATH_DASH.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
//        this.comboAnimation.put(GuHaoAnimations.GUHAO_BIU, (AttackAnimation) GuHaoAnimations.BIU.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
//        this.comboAnimation.put(GuHaoAnimations.GUHAO_DASH_2, (AttackAnimation) GuHaoAnimations.DENG_LONG.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
//
//        this.comboAnimation.put(Animations.RUSHING_TEMPO3, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
//        this.comboAnimation.put(Animations.RUSHING_TEMPO1, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
//        this.comboAnimation.put(Animations.RUSHING_TEMPO2, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
//        this.comboAnimation.put(StarAnimations.KATANA_SHEATH_DASH, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
//        this.comboAnimation.put(GuHaoAnimations.BIU, (AttackAnimation) Animations.REVELATION_TWOHAND.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK,false));
//        this.comboAnimation.put(GuHaoAnimations.SETTLEMENT, (AttackAnimation) GuHaoAnimations.DENG_LONG.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
//
//
//        this.comboAnimation.put(Animations.REVELATION_TWOHAND, (AttackAnimation) GuHaoAnimations.GUHAO_UCHIGATANA_SCRAP.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
        this.comboAnimation.put(WOMAnimations.KATANA_AUTO_1, (AttackAnimation) Animations.RUSHING_TEMPO2);
        this.comboAnimation.put(WOMAnimations.KATANA_AUTO_2, (AttackAnimation) Animations.RUSHING_TEMPO2);
        this.comboAnimation.put(WOMAnimations.KATANA_AUTO_3, (AttackAnimation) GuHaoAnimations.EF_UCHIGATANA_SHEATHING_DASH.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
        this.comboAnimation.put(Animations.TACHI_AUTO2, (AttackAnimation) Animations.RUSHING_TEMPO3);
        this.comboAnimation.put(GuHaoAnimations.HERRSCHER_AUTO_3, (AttackAnimation) Animations.RUSHING_TEMPO1);

        this.comboAnimation.put(GuHaoAnimations.GUHAO_DASH_2, (AttackAnimation) GuHaoAnimations.DENG_LONG.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
        this.comboAnimation.put(GuHaoAnimations.SETTLEMENT, (AttackAnimation) GuHaoAnimations.DENG_LONG.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));

        this.comboAnimation.put(Animations.RUSHING_TEMPO2, (AttackAnimation) GuHaoAnimations.GUHAO_BIU);
        this.comboAnimation.put(StarAnimations.KATANA_FATAL_DRAW_SECOND_NEW, (AttackAnimation) GuHaoAnimations.GUHAO_BIU);
        this.comboAnimation.put(GuHaoAnimations.EF_UCHIGATANA_SHEATHING_DASH, (AttackAnimation) GuHaoAnimations.GUHAO_BIU);
        this.comboAnimation.put(Animations.RUSHING_TEMPO3, (AttackAnimation) GuHaoAnimations.GUHAO_BIU);
        this.comboAnimation.put(Animations.RUSHING_TEMPO1, (AttackAnimation) GuHaoAnimations.GUHAO_BIU);

        this.comboAnimation.put(GuHaoAnimations.GUHAO_BIU, (AttackAnimation) GuHaoAnimations.GUHAO_UCHIGATANA_SCRAP.newTimePair(0.0F, 0.25F).addStateRemoveOld(EntityState.CAN_BASIC_ATTACK, false));
    }
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(IS_CTRL_DOWN);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
            ServerPlayerPatch executer = event.getPlayerPatch();
            DynamicAnimation animation = executer.getAnimator().getPlayerFor(null).getAnimation();
            if (animation == FalchionAnimations.FALCHION_SIDE || animation == FalchionAnimations.FALCHION_AUTO3) {
                event.getDamageSource().setStunType(StunType.NONE);
            }
        });

    }
    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID);
    }
    @Override
    public WeaponInnateSkill registerPropertiesToAnimation() {
        this.comboAnimation.values().forEach((animation) -> {
            animation.phases[0].addProperties(this.properties.get(0).entrySet());
        });

        return this;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public FriendlyByteBuf gatherArguments(LocalPlayerPatch executer, ControllEngine controllEngine) {
        Input input = executer.getOriginal().input;
        input.tick(false);
        int forward = input.up ? 1 : 0;
        int backward = input.down ? -1 : 0;
        int left = input.left ? 1 : 0;
        int right = input.right ? -1 : 0;
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(forward);
        buf.writeInt(backward);
        buf.writeInt(left);
        buf.writeInt(right);
        return buf;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Object getExecutionPacket(LocalPlayerPatch executer, FriendlyByteBuf args) {
        int forward = args.readInt();
        int backward = args.readInt();
        int left = args.readInt();
        int right = args.readInt();
        int vertic = forward + backward;
        int horizon = left + right;
        int animation;
        if (vertic == 0) {
            if (horizon == 0) {
                animation = 1;
            } else {
                animation = 2;
            }
        } else {
            animation = vertic >= 0 ? 0 : 1;
        }

        CPExecuteSkill packet = new CPExecuteSkill(executer.getSkill(this).getSlotId());
        packet.getBuffer().writeInt(animation);
        return packet;
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if(container.getExecuter().isLogicalClient()) {
            container.getDataManager().setDataSync(IS_CTRL_DOWN, Key.CTRL.isDown(), ((LocalPlayer) container.getExecuter().getOriginal()));
        }
    }

    /**
     * 保险
     */
    public static void register(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            IS_CTRL_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        });
    }

    //    @OnlyIn(Dist.CLIENT)
//    @Override
//    public boolean shouldDraw(SkillContainer container) {
//        PlayerPatch<?> executer = container.getExecuter();
//        EntityState playerState = executer.getEntityState();
//        return this.comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation().getRegistryName()) && playerState.canUseSkill() && playerState.inaction();
//    }
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void drawOnGui(BattleModeGui gui, SkillContainer container, PoseStack poseStack, float x, float y) {
//        poseStack.pushPose();
//        poseStack.translate(0.0, (float)gui.getSlidingProgression(), 0.0);
//        ResourceLocation name = this.getRegistryName();
//        RenderSystem.setShaderTexture(0, new ResourceLocation(name.getNamespace(), "textures/gui/skills/guhao_passive.png"));
//        GuiComponent.blit(poseStack, (int)x, (int)y, 24, 24, 0, 0, 1, 1, 1, 1);
//        poseStack.popPose();
//    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(GuHaoPassive.SHEATH);
        boolean isStop = executer.getOriginal().isSprinting();
        boolean isOnGround = executer.getOriginal().isOnGround();
        while (true) {
            if (executer.getTarget() != null && ((executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() >= 13 && (executer.getTarget().getHealth() <= executer.getTarget().getMaxHealth() * 0.1f) || (executer.getTarget().getHealth() <= 10.0f)) && !executer.getOriginal().isOnGround())) {
                executer.playAnimationSynchronized(WOMAnimations.AGONY_CLAWSTRIKE.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d) -> 0.75F)
                        .addEvents(AnimationProperty.StaticAnimationProperty.ON_BEGIN_EVENTS, AnimationEvent.create((entitypatch, animation, params) -> {
                    LivingEntity target = entitypatch.getTarget();
                    target.getLevel().addParticle(ParticleType.TWO_EYE.get(), target.getX(),target.getY(),target.getZ(),target.getX(),target.getEyeY(),target.getZ());
                    Vec3 viewVec = executer.getOriginal().getViewVector(1.0F);
                    target.teleportTo(executer.getOriginal().getX() + viewVec.x() * 1.85, executer.getOriginal().getY(), executer.getOriginal().getZ() + viewVec.z() * 1.85);
                    if (target.hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) target.removeEffect(EpicFightMobEffects.STUN_IMMUNITY.get());
                    if (target.hasEffect(com.guhao.star.regirster.Effect.REALLY_STUN_IMMUNITY.get())) target.removeEffect(com.guhao.star.regirster.Effect.REALLY_STUN_IMMUNITY.get());
                    if (!target.isAlive()) return;
                    entitypatch.playSound(com.guhao.init.Sounds.CHARGE,1.2f,1.1f,1.1f);
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
                            if (!target.isAlive()) return;
                            Random random = new Random();
                                    double distance = 500;
                                    // 生成指定数量的粒子
                                    for (int i = 0; i < 4; i++) {
                                        // 生成随机的偏移量
                                        for (int j = 0; j < 3; j++) {
                                            double startX = target.getX();
                                            double startY = target.getY();
                                            double startZ = target.getZ();
                                            double offsetX = (random.nextDouble()) * distance;
                                            double offsetY = (random.nextDouble()) * distance;
                                            double offsetZ = (random.nextDouble()) * distance;

                                            // 计算粒子的起点和终点
                                            double startOffsetX = startX + offsetX;
                                            double startOffsetY = startY + offsetY;
                                            double startOffsetZ = startZ + offsetZ;

                                            double endOffsetX = startX - offsetX;
                                            double endOffsetY = startY - offsetY;
                                            double endOffsetZ = startZ - offsetZ;

                                            // 添加粒子效果
                                            Level level = target.getLevel();
                                            level.addParticle(ParticleType.GUHAO_LASER.get(), startOffsetX, startOffsetY, startOffsetZ, endOffsetX, endOffsetY, endOffsetZ);
                                            level.addParticle(ParticleType.ONE_JC_BLOOD_JUDGEMENT_LONG.get(), startX, startY + 20, startZ, startX, startY - 20, startZ);
                                        }
                                    }
                            target.hurt(DamageSource.playerAttack((Player) entitypatch.getOriginal()).bypassArmor().damageHelmet().bypassInvul(), target.getHealth()*10.0f);
                            entitypatch.playSound(Sounds.SEKIRO, 1f, 1f, 1f);
                            if (target.getLevel() instanceof  ServerLevel _level) {
                                _level.playSound(null, new BlockPos(target.getX(), target.getY(), target.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(EpicFightSounds.LASER_BLAST.getLocation())), SoundSource.PLAYERS, 1, 1);
                                _level.playSound(null, new BlockPos(target.getX(), target.getY(), target.getZ()), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(com.guhao.init.Sounds.BIU.getLocation())), SoundSource.PLAYERS, 1, 1);
                            }
                            MinecraftForge.EVENT_BUS.unregister(this);
                        }
                    }.start(entitypatch.getOriginal().getLevel(), 40);
                }, AnimationEvent.Side.BOTH)), 0.0F);

                executer.getSkill(SkillSlots.WEAPON_INNATE).setStack(0);
                super.executeOnServer(executer, args);
                break;
            }
            if (this.comboAnimation.containsKey(executer.getAnimator().getPlayerFor(null).getAnimation())) {
                executer.playAnimationSynchronized(this.comboAnimation.get(executer.getAnimator().getPlayerFor(null).getAnimation()), 0.0F);
                super.executeOnServer(executer, args);
                break;
            }
            if (executer.getOriginal().isShiftKeyDown() && (executer.getSkill(SkillSlots.WEAPON_INNATE).getStack() >= 10)) {
                if (executer.getOriginal().hasEffect(Effect.GUHAO.get())) {
                    if (isSheathed) {
                        executer.playAnimationSynchronized(GuHaoAnimations.BLOOD_JUDGEMENT, -0.3F);
                        executer.getSkill(SkillSlots.WEAPON_INNATE).setStack(4);
                        executer.setStamina(executer.getStamina() * 0.66F);
                        super.executeOnServer(executer, args);
                    } else {
                        executer.playAnimationSynchronized(GuHaoAnimations.BLOOD_JUDGEMENT, 0.0F);
                        executer.getSkill(SkillSlots.WEAPON_INNATE).setStack(3);
                        executer.setStamina(executer.getStamina() * 0.5F);
                        super.executeOnServer(executer, args);
                    }
                } else {
                    executer.playAnimationSynchronized(GuHaoAnimations.SACRIFICE, 0.0F);
                    executer.getSkill(SkillSlots.WEAPON_INNATE).setStack(0);
                    executer.setStamina(0.0F);
                    super.executeOnServer(executer, args);
                }
                break;
            }
///////////////////////////////////////////////////////////////////////////
            if (executer.getSkill(SkillSlots.WEAPON_INNATE).getDataManager().getDataValue(IS_CTRL_DOWN)) {
                int i = args.readInt();
                executer.playAnimationSynchronized(this.animations[i], 0.0F);
                super.executeOnServer(executer, args);
                break;
            }
///////////////////////////////////////////////////////////////////////////
            if (isOnGround && !isStop && isSheathed) {
                executer.playAnimationSynchronized(GuHaoAnimations.SETTLEMENT, 0.0F);
                super.executeOnServer(executer, args);
            } else {
                    if (isSheathed) {
                        executer.playAnimationSynchronized(GuHaoAnimations.GUHAO_BATTOJUTSU_DASH, -0.694F);
                        super.executeOnServer(executer, args);
                    } else {
                        executer.playAnimationSynchronized(GuHaoAnimations.GUHAO_BATTOJUTSU_DASH, 0.0F);
                        super.executeOnServer(executer, args);
                    }
            }
            break;
        }
    }
}