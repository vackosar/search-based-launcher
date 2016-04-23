package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.WindowManager;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.SelectAction;
import com.vackosar.searchbasedlauncher.entity.YesNo;

import java.io.Serializable;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class KeyboardHiderSelector extends SelectAction<YesNo> implements Serializable {

    public static final YesNo DEFAULT = YesNo.No;

    @Expose private YesNo hidden = DEFAULT;
    @Inject private Activity activity;

    private void sync() {
        if (hidden.bool) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @Override
    protected void load() {
        super.load();
        sync();
    }

    @Override
    protected Enum<YesNo> getSelected() {
        return hidden;
    }

    @Override
    public void setSelected(Enum<YesNo> selected) {
        this.hidden = (YesNo) selected;
        sync();
        save();
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Hide Keyboard";
    }
}
