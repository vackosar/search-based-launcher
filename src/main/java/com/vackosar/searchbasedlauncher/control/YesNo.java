package com.vackosar.searchbasedlauncher.control;

import com.google.gson.annotations.Expose;

public enum YesNo {
    @Expose Yes(0, true),
    @Expose No(1, false);

    public int position;
    public boolean bool;

    YesNo(int position, boolean bool) {
        this.position = position;
        this.bool = bool;
    }

    public static YesNo valueOf(int position) {
        return YesNo.values()[position];
    }

    public static YesNo valueOf(boolean bool) {
        return bool? Yes: No;
    }
}
