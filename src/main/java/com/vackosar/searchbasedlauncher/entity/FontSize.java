package com.vackosar.searchbasedlauncher.entity;

public enum FontSize {
    pt5,
    pt6,
    pt7,
    pt8,
    pt9,
    pt10,
    pt11,
    pt12,
    pt13,
    pt14,
    pt15,
    pt16,
    pt17,
    pt18,
    pt19,
    pt20,
    pt21,
    pt22,
    pt23,
    pt24,
    pt25,
    pt26,
    pt27,
    pt28,
    pt29,
    pt30,
    ;

    public int getSize() {
        return Integer.valueOf(this.name().replaceFirst("pt", ""));
    }
}
