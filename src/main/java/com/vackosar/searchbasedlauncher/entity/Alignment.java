package com.vackosar.searchbasedlauncher.entity;

import android.view.Gravity;

public enum Alignment {
    left(Gravity.LEFT),
    center(Gravity.CENTER_HORIZONTAL),
    right(Gravity.RIGHT)
    ;

    private final int id;

    Alignment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
