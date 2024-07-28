package com.guhao;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class GuHaoColliderPreset {
    public static final Collider GUHAO_BATTOJUTSU_DASH = new MultiOBBCollider(
            new OBBCollider(1.65, 1.65, 2.1, 0.0, 1.1, -0.9),
            new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0),
            new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0),
            new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0),
            new OBBCollider(0.7, 0.7, 1.0, 0.0, 1.0, -1.0),
            new OBBCollider(1.5, 0.7, 1.0, 0.0, 1.0, -1.0));
    public static final Collider BIG_ATTACK = new MultiOBBCollider(10, 6.25, 2.75, 6.25, 0.0, 0.0, 0.0);
    public static final Collider SACRIFICE_ATTACK = new MultiOBBCollider(6, 0.64, 0.64, 1.983, 0.0, 0.0, -1.38);
    public static final Collider GUHAO = new MultiOBBCollider(4, 0.2, 0.2, 1.883, 0.0, 0.0, -1.28);
    public static final Collider ENDER_LASER = new MultiOBBCollider(10, 0.32, 0.32, 20.5, 0.0, 0.0, -20.2);
    public GuHaoColliderPreset() {
    }
}
