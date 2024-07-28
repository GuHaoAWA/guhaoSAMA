package com.guhao.capability;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum GuhaoWeaponCategories implements WeaponCategory {
    GUHAO;
    final int id;

    GuhaoWeaponCategories() {
        this.id = WeaponCategory.ENUM_MANAGER.assign(this);
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
