package com.guhao;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;

public class GuHaoColliderPreset {
    public static final Collider BIG_ATTACK = new MultiOBBCollider(10, 6.25, 2.75, 6.25, 0.0, 0.0, 0.0);
    public static final Collider GUHAO = new MultiOBBCollider(4, 0.4, 0.4, 1.66, 0.0, 0.0, -1.88);
    public GuHaoColliderPreset() {
    }
}
