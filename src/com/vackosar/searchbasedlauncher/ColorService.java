package com.vackosar.searchbasedlauncher;

import android.view.View;

/**
 * Created by vk on 05/07/2015.
 */
public class ColorService {
    private static int ACTIVE_COLOR = -16711936;
    private static int NORMAL_COLOR = -12303292;

    public int getActiveColor() {
        return ACTIVE_COLOR;
    }

    public int getNormalColor() {
        return NORMAL_COLOR;
    }

    public void setActive(View view) {
        view.setBackgroundColor(getActiveColor());
    }

    public void setNormal(View view) {
        view.setBackgroundColor(getNormalColor());
    }

    public void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void setInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public void setActive(boolean active, View view) {
        if (active) {
            setActive(view);
        } else {
            setNormal(view);
        }
    }
}
