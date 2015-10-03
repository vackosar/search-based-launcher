package com.vackosar.searchbasedlauncher.control;

import android.graphics.Color;
import android.widget.TextView;

public class ColorService {

    private static int ACTIVE_COLOR = -16711936;
    private static int NORMAL_COLOR = Color.WHITE;

    public int getActiveColor() {
        return ACTIVE_COLOR;
    }

    public int getNormalColor() {
        return NORMAL_COLOR;
    }

    public void setActive(TextView view) {
        view.setTextColor(getActiveColor());
    }

    public void setNormal(TextView view) {
        view.setTextColor(getNormalColor());
    }

    public void setVisible(TextView view) {
        view.setVisibility(TextView.VISIBLE);
    }

    public void setInvisible(TextView view) {
        view.setVisibility(TextView.INVISIBLE);
    }

    public void setActive(boolean active, TextView view) {
        if (active) {
            setActive(view);
        } else {
            setNormal(view);
        }
    }
}
