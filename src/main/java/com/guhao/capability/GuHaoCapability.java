package com.guhao.capability;

import com.dfdyz.epicacg.registry.MyAnimations;
import com.guhao.GuHaoAnimations;
import com.guhao.GuHaoColliderPreset;
import com.guhao.init.ParticleType;
import com.nameless.falchion.gameasset.FalchionAnimations;
import com.nameless.falchion.gameasset.FalchionSkills;
import net.minecraft.world.item.Item;
import reascer.efmpirates.gameasset.OPXEFAnimations;
import reascer.infernal.gameasset.InfernalAnimations;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.BattojutsuPassive;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapability;

import java.util.function.Function;

public class GuHaoCapability {
    public static final Function<Item, CapabilityItem.Builder> GUHAO = (item) -> {
        WeaponCapability.Builder builder = WeaponCapability.builder()
                .category(CapabilityItem.WeaponCategories.SPEAR)
                .styleProvider((playerpatch) -> {
                    if (playerpatch instanceof PlayerPatch<?> player) {
                        if (player.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().hasData(BattojutsuPassive.SHEATH) &&
                                player.getSkill(SkillSlots.WEAPON_PASSIVE).getDataManager().getDataValue(BattojutsuPassive.SHEATH)) {
                            return CapabilityItem.Styles.SHEATH;
                        }
                    }
                    return CapabilityItem.Styles.TWO_HAND;
        }).collider(GuHaoColliderPreset.GUHAO).hitSound(EpicFightSounds.BLADE_HIT).hitParticle((HitParticleType) ParticleType.EYE.get()).canBePlacedOffhand(false)
                .newStyleCombo(CapabilityItem.Styles.TWO_HAND,
                        GuHaoAnimations.GUHAO_SHEATHING_AUTO1, //1
                        FalchionAnimations.FALCHION_AUTO2,  //2
                        FalchionAnimations.FALCHION_AUTO1,  //3
                        WOMAnimations.AGONY_AUTO_1, //4
                        MyAnimations.BATTLE_SCYTHE_AUTO2,   //5
                        FalchionAnimations.FALCHION_AUTO3,  //6
                        WOMAnimations.ANTITHEUS_ASCENDED_AUTO_3,    //7
                        WOMAnimations.ENDERBLASTER_ONEHAND_AUTO_4,  //8
                        WOMAnimations.STAFF_AUTO_2, //9
                        WOMAnimations.MOONLESS_AUTO_3,  //10
                        Animations.LONGSWORD_AUTO3, //11
                        InfernalAnimations.INFERNAL_KCIK13, //12
                        OPXEFAnimations.SANJI_AUTO_3, //13
                        MyAnimations.BATTLE_SCYTHE_AUTO2, //14
                        OPXEFAnimations.SANJI_DIABLE_AUTO_2,    //15
                        WOMAnimations.MOONLESS_AUTO_2,  //16
                        WOMAnimations.RUINE_AUTO_2, //17

                        GuHaoAnimations.ENDER,
                        GuHaoAnimations.NB_ATTACK
                )
                .newStyleCombo(CapabilityItem.Styles.SHEATH,
                        GuHaoAnimations.GUHAO_SHEATHING_AUTO1, //18
                        Animations.UCHIGATANA_SHEATHING_DASH,   //19
                        Animations.UCHIGATANA_SHEATH_AIR_SLASH  //20
                )
                .passiveSkill(EpicFightSkills.BATTOJUTSU_PASSIVE)
                .newStyleCombo(CapabilityItem.Styles.MOUNT, Animations.SPEAR_MOUNT_ATTACK)
                .comboCancel((style) -> false)
                .innateSkill(CapabilityItem.Styles.SHEATH, (itemStack) -> FalchionSkills.FALCHION_ART)
                .innateSkill(CapabilityItem.Styles.TWO_HAND, (itemStack) -> FalchionSkills.FALCHION_ART)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.WALK, WOMAnimations.KATANA_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.CHASE, Animations.BIPED_HOLD_SPEAR)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.RUN, WOMAnimations.KATANA_WALK)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_SPEAR)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.BLOCK, WOMAnimations.AGONY_GUARD)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SNEAK, Animations.BIPED_WALK_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.SWIM, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FLOAT, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.TWO_HAND, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.IDLE, WOMAnimations.ANTITHEUS_ASCENDED_IDLE)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.KNEEL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.WALK, WOMAnimations.ANTITHEUS_ASCENDED_WALK)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.CHASE, WOMAnimations.ANTITHEUS_ASCENDED_RUN)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.RUN, WOMAnimations.ANTITHEUS_ASCENDED_RUN)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.SNEAK, Animations.BIPED_SNEAK)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.SWIM, Animations.BIPED_SWIM)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.FLOAT, Animations.BIPED_FLOAT)
                .livingMotionModifier(CapabilityItem.Styles.SHEATH, LivingMotions.FALL, Animations.BIPED_HOLD_UCHIGATANA_SHEATHING);
        return builder;
    };

    public GuHaoCapability() {
    }

    public static void register(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put("guhao", GUHAO);
    }
}
