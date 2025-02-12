package com.guhao.utils;

import com.guhao.GuHaoAnimations;
import com.guhao.star.efmex.StarAnimations;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;

import java.util.Arrays;

public record ArrayUtils() {
    static final StaticAnimation[] EYES;
    static {
        EYES = new StaticAnimation[] {

                WOMAnimations.KATANA_AUTO_1,
                WOMAnimations.KATANA_AUTO_2,
                WOMAnimations.KATANA_AUTO_3,
                Animations.TACHI_AUTO2,
                GuHaoAnimations.GUHAO_DASH_2,
                GuHaoAnimations.GUHAO_DASH,
                GuHaoAnimations.HERRSCHER_AUTO_3,

                Animations.RUSHING_TEMPO3,
                Animations.RUSHING_TEMPO1,
                Animations.RUSHING_TEMPO2,
                GuHaoAnimations.EF_UCHIGATANA_SHEATHING_DASH,
                GuHaoAnimations.SETTLEMENT,
                StarAnimations.KATANA_FATAL_DRAW_SECOND_NEW,

                GuHaoAnimations.GUHAO_BIU,
        };
    }
    public static boolean isEyes(StaticAnimation staticAnimation) {
        return Arrays.asList(EYES).contains(staticAnimation);
    }
}
