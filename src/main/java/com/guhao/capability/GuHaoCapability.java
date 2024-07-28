package com.guhao.capability;

import com.guhao.GuHaoAnimations;
import com.guhao.GuHaoColliderPreset;
import com.guhao.init.ParticleType;
import com.guhao.skills.GuHaoPassive;
import com.guhao.skills.GuHaoSkills;
import net.minecraft.world.item.Item;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;

public class GuHaoCapability {
    public static final Function<Item, CapabilityItem.Builder> GUHAO = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.UCHIGATANA)
                .styleProvider((entitypatch) -> {
                    if (entitypatch instanceof PlayerPatch<?> playerpatch) {
                        if (playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(GuHaoPassive.SHEATH) &&
                                playerpatch.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(GuHaoPassive.SHEATH)) {
                            return CapabilityItem.Styles.SHEATH;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
                })
                .passiveSkill(GuHaoSkills.GUHAO_PASSIVE)
                .hitSound(EpicFightSounds.BLADE_HIT)
                .hitParticle((HitParticleType) ParticleType.EYE.get())
                .collider(GuHaoColliderPreset.GUHAO)
                .canBePlacedOffhand(false)
                .newStyleCombo(CapabilityItem.Styles.SHEATH,
                        GuHaoAnimations.GUHAO_UCHIGATANA_SHEATHING_AUTO,
                        GuHaoAnimations.GUHAO_UCHIGATANA_SHEATHING_DASH,
                        GuHaoAnimations.GUHAO_UCHIGATANA_SHEATH_AIR_SLASH)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        Animations.TACHI_AUTO3,
                        Animations.UCHIGATANA_AUTO1,
                        Animations.LONGSWORD_AUTO2,
                        Animations.UCHIGATANA_AUTO3,
                        GuHaoAnimations.GUHAO_BIU,
                        Animations.SWEEPING_EDGE,
                        GuHaoAnimations.GUHAO_DASH,
                        GuHaoAnimations.NB_ATTACK)
                .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
                .innateSkill(CapabilityItem.Styles.SHEATH, (itemstack) -> GuHaoSkills.SACRIFICE)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemstack) -> GuHaoSkills.SACRIFICE)
                .comboCancel((style) -> false)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, Animations.BIPED_WALK_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_WALK_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_WALK_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.IDLE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.WALK, Animations.BIPED_WALK_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.CHASE, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.RUN, Animations.BIPED_RUN_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.SNEAK, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, Animations.UCHIGATANA_GUARD);

        return builder;
    };

    public GuHaoCapability() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put("guhao", GUHAO);
    }
}
