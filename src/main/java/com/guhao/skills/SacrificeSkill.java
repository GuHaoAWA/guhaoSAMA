package com.guhao.skills;

import com.guhao.GuHaoAnimations;
import com.guhao.init.Effect;
import com.nameless.falchion.gameasset.FalchionAnimations;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.BattojutsuPassive;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class SacrificeSkill extends WeaponInnateSkill {
    private final Minecraft mc = Minecraft.getInstance();
    private final StaticAnimation[] animations;
    private static final UUID EVENT_UUID = UUID.fromString("d706b5bc-b98b-cc49-b83e-16ae590db349");

    public SacrificeSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
        this.animations = new StaticAnimation[]{FalchionAnimations.FALCHION_FORWARD, FalchionAnimations.FALCHION_BACKWARD, FalchionAnimations.FALCHION_SIDE};
    }

    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID, (event) -> {
            ServerPlayerPatch executer = event.getPlayerPatch();
            DynamicAnimation animation = executer.getAnimator().getPlayerFor(null).getAnimation();
            if (animation == FalchionAnimations.FALCHION_SIDE || animation == FalchionAnimations.FALCHION_AUTO3) {
                event.getDamageSource().setStunType(StunType.NONE);
            }
        });
    }

    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_POST, EVENT_UUID);
    }

    public WeaponInnateSkill registerPropertiesToAnimation() {
        return this;
    }

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
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        while (true) {
            boolean isSheathed = executer.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(GuHaoPassive.SHEATH);
            if (mc.options.keyShift.isDown() && (executer.getSkill(GuHaoSkills.SACRIFICE).getStack() >= 10)) {
                if (executer.getOriginal().hasEffect(Effect.GUHAO.get())) {
                    if (isSheathed) {
                        executer.playAnimationSynchronized(GuHaoAnimations.BLOOD_JUDGEMENT, -0.196F);
                        executer.getSkill(GuHaoSkills.SACRIFICE).setStack(6);
                        executer.setStamina(executer.getStamina() * 0.65F);
                    } else {
                        executer.playAnimationSynchronized(GuHaoAnimations.BLOOD_JUDGEMENT, 0.0F);
                        executer.getSkill(GuHaoSkills.SACRIFICE).setStack(5);
                        executer.setStamina(executer.getStamina() * 0.5F);
                    }
                } else {
                executer.playAnimationSynchronized(GuHaoAnimations.SACRIFICE, 0.0F);
                executer.getSkill(GuHaoSkills.SACRIFICE).setStack(0);
                executer.setStamina(0F);
                }
                super.executeOnServer(executer, args);
                break;
            }
///////////////////////////////////////////////////////////////////////////
            if (mc.options.keySprint.isDown()) {
                int i = args.readInt();
                executer.playAnimationSynchronized(this.animations[i], 0.0F);
                super.executeOnServer(executer, args);
                break;
            }
///////////////////////////////////////////////////////////////////////////
            if (isSheathed) {
                executer.playAnimationSynchronized(GuHaoAnimations.GUHAO_BATTOJUTSU_DASH, -0.694F);
            } else {
                executer.playAnimationSynchronized(GuHaoAnimations.GUHAO_BATTOJUTSU_DASH, 0.0F);
            }
            super.executeOnServer(executer, args);
            break;
        }
    }
}
