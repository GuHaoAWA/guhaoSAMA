package com.guhao.skills;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;

import static com.guhao.Guhao.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus= Mod.EventBusSubscriber.Bus.FORGE)
public class GuHaoSkills {
    public static Skill GUHAOPASSIVE;

    public GuHaoSkills() {
    }

    public static void registerSkills() {
        SkillManager.register(GUHAOPASSIVE::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setActivateType(Skill.ActivateType.ONE_SHOT).setResource(Skill.Resource.COOLDOWN), MODID, "guhaopassive");
    }

    @SubscribeEvent
    public static void buildSkillEvent(SkillBuildEvent onBuild) {

        GUHAOPASSIVE = onBuild.build(MODID, "guhaopassive");


    }
}
