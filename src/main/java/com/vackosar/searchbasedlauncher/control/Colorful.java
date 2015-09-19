package com.vackosar.searchbasedlauncher.control;

import android.view.View;

public abstract class Colorful {
    ColorService colorService = new ColorService();

    public void setActivatedColor () {
        colorService.setActive(getView());
    }
    public void setNormalColor() {
        colorService.setNormal(getView());
    }
    public  void setActivatedColor (boolean activated) {
        if (activated) {
            setActivatedColor();
        } else {
            setNormalColor();
        }
    }
    public  void setVisible() {
        colorService.setVisible(getView());
    }
    public  void setInvisible() {
        colorService.setInvisible(getView());
    }
    public  void setVisible(boolean visible) {
        if (visible) {
            setVisible();
        } else {
            setInvisible();
        }
    }

    abstract View getView();
}
