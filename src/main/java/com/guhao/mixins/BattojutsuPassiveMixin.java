package com.guhao.mixins;

import com.guhao.GuHaoAnimations;
import com.guhao.init.Items;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.skill.BattojutsuPassive;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import static yesman.epicfight.skill.BattojutsuPassive.SHEATH;

@Mixin(value = BattojutsuPassive.class, remap = false, priority = 114514)
public class BattojutsuPassiveMixin extends Skill {
    public BattojutsuPassiveMixin(Builder<? extends Skill> builder) {
        super(builder);
    }

    @Inject(at = @At("HEAD"), method = "setConsumption", cancellable = true)
    public void setConsumption(SkillContainer container, float value, CallbackInfo ci) {
        PlayerPatch<?> executer = container.getExecuter();
        if (!executer.isLogicalClient()) {
            if (container.getMaxResource() < value) {
                ServerPlayer serverPlayer = (ServerPlayer) executer.getOriginal();
                container.getDataManager().setDataSync(SHEATH, true, serverPlayer);
                ((ServerPlayerPatch) container.getExecuter()).modifyLivingMotionByCurrentItem();
                if (!(serverPlayer.getMainHandItem().getItem() == Items.GUHAO.get())) {
                    SPPlayAnimation msg3 = new SPPlayAnimation(Animations.BIPED_UCHIGATANA_SCRAP, serverPlayer.getId(), 0.0F);
                    EpicFightNetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(msg3, serverPlayer);
                } else {
                    SPPlayAnimation msg3 = new SPPlayAnimation(GuHaoAnimations.PASSIVE, serverPlayer.getId(), 0.0F);
                    EpicFightNetworkManager.sendToAllPlayerTrackingThisEntityWithSelf(msg3, serverPlayer);
                }
            }
        }
        super.setConsumption(container, value);
        ci.cancel();
    }
}



