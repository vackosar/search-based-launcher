package com.vackosar.searchbasedlauncher.entity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectAction<T extends Enum<T>> extends Action {

    @Inject private Activity activity;

    public abstract Enum<T> getSelected();

    protected abstract void setSelected(Enum<T> value);

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
}
