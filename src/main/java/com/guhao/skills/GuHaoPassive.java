package com.guhao.skills;

import com.guhao.GuHaoAnimations;
import com.guhao.init.Effect;
import com.guhao.init.Items;
import com.guhao.init.Key;
import com.guhao.init.ParticleType;
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedle;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Option;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.lwjgl.glfw.GLFW;
import yesman.epicfight.api.animation.types.DodgeAnimation;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.passive.PassiveSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.Random;
import java.util.UUID;

public class GuHaoPassive extends PassiveSkill {

    public static final SkillDataManager.SkillDataKey<Boolean> SHEATH;
    public static SkillDataManager.SkillDataKey<Boolean> IS_RIGHT_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
    private static final UUID EVENT_UUID;

    public GuHaoPassive(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }
    public static void register(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            IS_RIGHT_DOWN = SkillDataManager.SkillDataKey.createDataKey(SkillDataManager.ValueType.BOOLEAN);
        });
    }
    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getDataManager().registerData(SHEATH);
        container.getDataManager().registerData(IS_RIGHT_DOWN);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID, (event) -> {
            if (!(event.getAnimation() instanceof DodgeAnimation)) {
                container.getSkill().setConsumptionSynchronize(event.getPlayerPatch(), 0.0F);
                container.getSkill().setStackSynchronize(event.getPlayerPatch(), 0);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, (event) -> this.onReset(container));
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID, (event) -> {
//            if (container.getExecuter().getOriginal().isUsingItem()) {
            if (container.getExecuter().getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(IS_RIGHT_DOWN)) {
//                CPExecuteSkill cpExecuteSkill = new CPExecuteSkill(container.getExecuter().getSkill(this).getSlotId());
//                ClientEngine.getInstance().controllEngine.addPacketToSend(cpExecuteSkill);
                container.getExecuter().getOriginal().stopUsingItem();
                event.setCanceled(true);
                container.getExecuter().playAnimationSynchronized(GuHaoAnimations.JIANQIE,0.0F);

            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID, (event) -> {
            if (event.getDamageSource().getEntity() instanceof LivingEntity livingEntity) {
                Level world = livingEntity.getLevel();
                Random random = new Random();
                int count = 1;
                float damage = random.nextFloat(20F, 30F);
                Vec3 center = livingEntity.position().add(0, livingEntity.getEyeHeight() / 2, 0);
                float degreesPerNeedle = 360f / count;
                for (int i = 0; i < count; i++) {
                    Vec3 offset = new Vec3(0, Math.random(), .55).normalize().scale(livingEntity.getBbWidth() + 2.75f).yRot(degreesPerNeedle * i * Mth.DEG_TO_RAD);
                    Vec3 spawn = center.add(offset);
                    Vec3 motion = center.subtract(spawn).normalize();

                    BloodNeedle needle = new BloodNeedle(world, event.getPlayerPatch().getOriginal());
                    needle.moveTo(spawn);
                    needle.shoot(motion.scale(.45f));
                    needle.setDamage(damage);
                    needle.setScale(1.2f);
                    world.addFreshEntity(needle);
                }
            }
        });
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (container.getExecuter().getOriginal().getMainHandItem().getItem() == Items.GUHAO.get() && container.getExecuter().getOriginal().hasEffect(Effect.GUHAO.get())) {
            container.getExecuter().getOriginal().getLevel().addParticle(ParticleType.RING.get(), container.getExecuter().getOriginal().getX(), container.getExecuter().getOriginal().getY() + 0.05, container.getExecuter().getOriginal().getZ(), 0, 0, 0);
        }
        if(container.getExecuter().isLogicalClient()) {
            container.getDataManager().setDataSync(IS_RIGHT_DOWN, Key.RIGHT.isDown(), ((LocalPlayer) container.getExecuter().getOriginal()));
        }
//        int nnL =4;
//        float min = -0.19f;
//        float max = -2.85f;
//        int nL = 5;
//        for (int iiL = 0;iiL < nnL; ++iiL){
//            float rz = min +(max- min)* new Random().nextFloat();
//            OpenMatrix4f transformMatrix = container.getExecuter().getArmature().getBindedTransformFor(container.getExecuter().getArmature().getPose(0.0F), Armatures.BIPED.toolR);
//            transformMatrix.translate(new Vec3f(0.0F,0.0F,-rz));
//            OpenMatrix4f.mul(
//                    new OpenMatrix4f().rotate(-(float)Math.toRadians(container.getExecuter().getOriginal().yBodyRotO) + 180.0F, new Vec3f(0.0F,1.0F, 0.0F)),
//                    transformMatrix,transformMatrix);
//            for(int iL=0;iL<nL; ++iL) {
//                container.getExecuter().getOriginal().getLevel().addParticle(
//                        EpicFightParticles.BLOOD.get(), //粒子类型
//                        transformMatrix.m30 + container.getExecuter().getOriginal().getX(), // X 坐标
//// Y 坐标
//                        transformMatrix.m31 + container.getExecuter().getOriginal().getY(),
//                        transformMatrix.m32 + container.getExecuter().getOriginal().getZ(), //2 坐标
//                        (float) 0.0, (float) -0.01, (float) 0.0);
//            }
//
//        }
    }
    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DODGE_SUCCESS_EVENT, EVENT_UUID);
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
