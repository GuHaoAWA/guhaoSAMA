package com.guhao.skills;

import com.guhao.client.particle.par.RingParticle;
import com.guhao.init.Effect;
import com.guhao.init.Items;
import com.guhao.init.ParticleType;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class GuHaoPassive extends PassiveSkill {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public static final SkillDataManager.SkillDataKey<Boolean> SHEATH;
    private static final UUID EVENT_UUID;
    public static final SkillDataManager.SkillDataKey<Boolean> PARTICLE;
    public static final SkillDataManager.SkillDataKey<Boolean> LAPSE;
    public static final SkillDataManager.SkillDataKey<Boolean> BASIC_ATTACK;
    public static final SkillDataManager.SkillDataKey<Boolean> IDLE;

    public GuHaoPassive(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(SHEATH);
        container.getDataManager().registerData(LAPSE);
        container.getDataManager().registerData(PARTICLE);
        container.getDataManager().registerData(BASIC_ATTACK);
        container.getDataManager().registerData(IDLE);
        container.getDataManager().setData(IDLE, false);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
            if (!(event.getAnimation() instanceof DodgeAnimation)) {
                container.getSkill().setConsumptionSynchronize(event.getPlayerPatch(), 0.0F);
                container.getSkill().setStackSynchronize(event.getPlayerPatch(), 0);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> {
            this.onReset(container);
        });
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (container.getExecuter().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get() && container.getExecuter().getOriginal().hasEffect(Effect.GUHAO.get())) {
            executorService.submit(() -> container.getExecuter().getOriginal().getLevel().addParticle(ParticleType.RING.get(), container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY() + 0.05, container.getExecuter().getOriginal().getZ(), 0, 0, 0));
        }
    }
    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
    }
    @Override
    public void onReset(SkillContainer container) {
        PlayerPatch<?> executer = container.getExecuter();
        if (!executer.isLogicalClient() && container.getDataManager().getDataValue(SHEATH)) {
            ServerPlayerPatch playerpatch = (ServerPlayerPatch)executer;
            container.getDataManager().setDataSync(SHEATH, false, playerpatch.getOriginal());
            playerpatch.modifyLivingMotionByCurrentItem();
            container.getSkill().setConsumptionSynchronize(playerpatch, 0.0F);
        }

    }
    @Override
    public void setConsumption(SkillContainer container, float value) {
        PlayerPatch<?> executer = container.getExecuter();
        if (!executer.isLogicalClient() && container.getMaxResource() < value) {
            ServerPlayer serverPlayer = (ServerPlayer)executer.getOriginal();
            container.getDataManager().setDataSync(SHEATH, true, serverPlayer);
            ((ServerPlayerPatch)container.getExecuter()).modifyLivingMotionByCurrentItem();
            SPPlayAnimation msg3 = new SPPlayAnimation(Animations.BIPED_UCHIGATANA_SCRAP, serverPlayer.getId(), 0.0F);
            EpicFightNetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(msg3, serverPlayer);
        }

        super.setConsumption(container, value);
    }
    @Override
    public boolean shouldDeactivateAutomatically(PlayerPatch<?> executer) {
        return true;
    }

    @Override
    public float getCooldownRegenPerSecond(PlayerPatch<?> player) {
        return player.getOriginal().isUsingItem() ? 0.0F : 1.0F;
    }

    static {
        SHEATH = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        EVENT_UUID = UUID.fromString("a416c93a-42cb-11eb-b378-0242ac130002");
        LAPSE = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        BASIC_ATTACK = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        IDLE = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        PARTICLE = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
    }
}
