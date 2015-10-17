package com.vackosar.searchbasedlauncher.entity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;

public abstract class SelectAction<T extends Enum<T>> extends Action {

    @Inject private Activity activity;

    @Expose protected Enum<T> selected;
    @Inject private SingletonPersister<Action> persister;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        load();
    }

    private void save() {
        persister.save(this);
    }

    private void load() {
        persister.load(this);
    }

    public void act() {
        List<String> items = new ArrayList<>();
        final Enum[] values = selected.getClass().getEnumConstants();
        for (Object o: values) {
            items.add(o.toString());
        }
        AlertDialog.OnClickListener listener = new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = selected.getClass().getEnumConstants()[which];
            }
        };
        new AlertDialog.Builder(activity)
                .setTitle(getName() + "\nCurrent: " + selected)
                .setItems(items.toArray(new String[items.size()]), listener)
                .show();
    }
}
