package com.guhao.skills;

import com.guhao.init.Effect;
import com.guhao.init.Items;
import com.guhao.init.ParticleType;
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

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuHaoPassive extends PassiveSkill {

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    public static final SkillDataManager.SkillDataKey<Boolean> SHEATH;
    private static final UUID EVENT_UUID;

    public GuHaoPassive(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(SHEATH);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
            if (!(event.getAnimation() instanceof DodgeAnimation)) {
                container.getSkill().setConsumptionSynchronize(event.getPlayerPatch(), 0.0F);
                container.getSkill().setStackSynchronize(event.getPlayerPatch(), 0);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> this.onReset(container));


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
        if (player.getOriginal().isUsingItem()) return 0.0F;
        if (player.getSkill(GuHaoSkills.GUHAO_PASSIVE).getDataManager().getDataValue(SHEATH)) return 0.0F;
        return 1.0F;
    }

    static {
        SHEATH = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        EVENT_UUID = UUID.fromString("a416c93a-42cb-11eb-b378-0242ac130002");
    }
}
