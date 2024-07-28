package com.guhao.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class MagicDamageSource extends DamageSource {
    private final Player player;

    public MagicDamageSource(Player player) {
        super("magic");
        this.player = player;
        this.setMagic();
    }

    @Override
    public Entity getDirectEntity() {
        return player;
    }

    @Override
    public boolean isFire() {
        return false;
    }

    @Override
    public boolean isProjectile() {
        return false;
    }

    @Override
    public boolean isMagic() {
        return true;
    }
}