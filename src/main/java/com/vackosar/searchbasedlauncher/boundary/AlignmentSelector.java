package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.Alignment;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AlignmentSelector extends SelectAction<Alignment> {

    public static final Alignment DEFAULT = Alignment.center;

    @Inject private Activity activity;
    @Expose private Alignment alignment = DEFAULT;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        load();
    }

    @Override
    protected Enum<Alignment> getSelected() {
        return alignment;
    }

    @Override
    public void setSelected(Enum<Alignment> selected) {
        this.alignment = (Alignment) selected;
        save();
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Text Alignment";
    }

    public int getAligmentId() {
        return alignment.getId();
    }


}
