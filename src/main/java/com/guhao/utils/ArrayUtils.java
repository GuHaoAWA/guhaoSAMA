package com.guhao.utils;

import com.guhao.GuHaoAnimations;
import reascer.wom.gameasset.WOMAnimations;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;

import java.util.Arrays;

public class ArrayUtils {
    static final StaticAnimation[] EYES;
    static {
        EYES = new StaticAnimation[] {

                Animations.TACHI_AUTO3,
                Animations.UCHIGATANA_AUTO1,
                Animations.UCHIGATANA_AUTO3,
                Animations.LONGSWORD_AUTO2,
                GuHaoAnimations.GUHAO_DASH,
                GuHaoAnimations.GUHAO_BIU,
                GuHaoAnimations.GUHAO_DASH,

                Animations.RUSHING_TEMPO3,
                Animations.RUSHING_TEMPO1,
                Animations.RUSHING_TEMPO2,
                WOMAnimations.RUINE_AUTO_1,
                GuHaoAnimations.BIU,
                WOMAnimations.KATANA_AUTO_3,

                Animations.REVELATION_TWOHAND
        };
    }
    public static boolean isEyes(StaticAnimation staticAnimation) {
        return Arrays.asList(EYES).contains(staticAnimation);
    }
}
