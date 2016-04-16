package com.vackosar.searchbasedlauncher.boundary;

import com.google.gson.annotations.Expose;
import com.vackosar.searchbasedlauncher.entity.AppsType;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ItemListSelector extends SelectAction<AppsType> {

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
        save();
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Select List";
    }

    public AppsType getAppsType() {
        return appsType;
    }
}
