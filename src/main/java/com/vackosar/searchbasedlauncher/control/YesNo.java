package com.vackosar.searchbasedlauncher.control;

public enum YesNo {
    Yes(0, true),
    No(1, false);

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
