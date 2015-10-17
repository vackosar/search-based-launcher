package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.control.AppsType;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ItemListSelector extends SelectAction<AppsType> {

    @Inject private Activity activity;

    public static final AppsType DEFAULT_SELECTED = AppsType.normal;

    public ItemListSelector() {
        this.selected = DEFAULT_SELECTED;
    }

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public String getName() {
        return "List Selection";
    }

    public AppsType getAppType() {
        return (AppsType) selected;
    }
}
