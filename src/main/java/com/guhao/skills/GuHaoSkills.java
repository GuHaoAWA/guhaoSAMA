package com.guhao.skills;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

import static com.guhao.Guhao.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class GuHaoSkills {

    public static Skill SACRIFICE;
    public static Skill GUHAO_PASSIVE;
    public GuHaoSkills() {
    }

    public static void registerSkills() {
        SkillManager.register(SacrificeSkill::new, WeaponInnateSkill.createWeaponInnateBuilder(), "guhao", "sacrifice");
        SkillManager.register(GuHaoPassive::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(Skill.ActivateType.DURATION).setResource(Skill.Resource.COOLDOWN),MODID, "guhao_passive");
    }

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent event) {

        WeaponInnateSkill sacrifice = event.build(MODID, "sacrifice");
        Skill guhao_passive = event.build(MODID, "guhao_passive");

        SACRIFICE = sacrifice;
        GUHAO_PASSIVE = guhao_passive;

    }
}