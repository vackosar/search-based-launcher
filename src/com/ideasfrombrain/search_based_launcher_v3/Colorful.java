package com.ideasfrombrain.search_based_launcher_v3;

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

    View getView();
}
