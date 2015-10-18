package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.control.AppsType;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ItemListSelector extends SelectAction<AppsType> {

    @Inject private Activity activity;
    @Expose private AppsType appsType;

    public static final AppsType DEFAULT_SELECTED = AppsType.normal;

    public ItemListSelector() {
        this.appsType = DEFAULT_SELECTED;
    }

    @Override
    protected Enum<AppsType> getSelected() {
        return appsType;
    }

    @Override
    public void setSelected(Enum<AppsType> selected) {
        appsType = (AppsType) selected;
    }

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public String getName() {
        return "List Selection";
    }

    public AppsType getAppsType() {
        return appsType;
    }
}
