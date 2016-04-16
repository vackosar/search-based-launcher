package com.vackosar.searchbasedlauncher.entity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;

public abstract class SelectAction<T extends Enum<T>> extends Action {

    @Inject private Activity activity;
    @Inject private SingletonPersister<Action> persister;

    public SelectAction() {}

    protected void setActivity(Activity activity) {
        this.activity = activity;
        persister = new SingletonPersister<>(activity.getApplicationContext());
        load();
    }

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        load();
    }

    protected void save() {
        persister.save(this);
    }

    protected void load() {
        persister.load(this);
    }

    public void act() {
        List<String> items = new ArrayList<>();
        final Enum[] values = getSelected().getClass().getEnumConstants();
        for (Object o: values) {
            items.add(o.toString());
        }
        AlertDialog.OnClickListener listener = new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setSelected(getSelected().getClass().getEnumConstants()[which]);
            }
        };
        new AlertDialog.Builder(activity)
                .setTitle(getName() + "\nCurrent: " + getSelected())
                .setItems(items.toArray(new String[items.size()]), listener)
                .show();
    }

    protected abstract Enum<T> getSelected();

    public abstract void setSelected(Enum<T> selected);

}
