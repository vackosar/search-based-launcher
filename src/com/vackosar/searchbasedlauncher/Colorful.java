package com.vackosar.searchbasedlauncher;

import android.view.View;

public interface Colorful {
    ColorService colorService = new ColorService();

    default void setActivatedColor () {
        colorService.setActive(getView());
    }
    default void setNormalColor() {
        colorService.setNormal(getView());
    }
    default void setActivatedColor (boolean activated) {
        if (activated) {
            setActivatedColor();
        } else {
            setNormalColor();
        }
    }
    default void setVisible() {
        colorService.setVisible(getView());
    }
    default void setInvisible() {
        colorService.setInvisible(getView());
    }
    default void setVisible(boolean visible) {
        if (visible) {
            setVisible();
        } else {
            setInvisible();
        }
    }

    View getView();
}
