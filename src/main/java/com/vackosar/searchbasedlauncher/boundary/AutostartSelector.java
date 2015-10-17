package com.vackosar.searchbasedlauncher.boundary;

import com.vackosar.searchbasedlauncher.control.YesNo;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import java.io.Serializable;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AutostartSelector extends SelectAction<YesNo> implements Serializable {

    public AutostartSelector() {
        selected = YesNo.Yes;
    }

    public boolean isOn() {
        return ((YesNo) selected).bool;
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
