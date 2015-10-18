package com.vackosar.searchbasedlauncher.boundary;

import com.google.gson.annotations.Expose;
import com.vackosar.searchbasedlauncher.control.YesNo;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import java.io.Serializable;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AutostartSelector extends SelectAction<YesNo> implements Serializable {

    @Expose private YesNo autostart;

    public AutostartSelector() {
        autostart = YesNo.Yes;
    }

    @Override
    protected Enum<YesNo> getSelected() {
        return autostart;
    }

    @Override
    public void setSelected(Enum<YesNo> selected) {
        this.autostart = (YesNo) selected;
        save();
    }

    public boolean isOn() {
        return autostart.bool;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Autostart Single Result";
    }
}
