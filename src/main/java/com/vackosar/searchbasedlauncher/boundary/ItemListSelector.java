package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.control.AppsType;
import com.vackosar.searchbasedlauncher.entity.SelectAction;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ItemListSelector extends SelectAction<AppsType> implements AdapterView.OnItemSelectedListener {

    @Inject private EventManager eventManager;
    @Inject private Activity activity;
    @Inject private SingletonPersister<ItemListSelector> persister;

    public static final AppsType DEFAULT_SELECTED = AppsType.normal;

    @Expose private AppsType selected = DEFAULT_SELECTED;

    @SuppressWarnings("unused")
    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        load();
    }

    private void load() {
        persister.load(this);
    }

    public void save() {
        persister.save(this);
    }

    public AppsType getSelected() {
        return selected;
    }

    @Override
    public void setSelected(Enum<AppsType> value) {
        selected = (AppsType) value;
        save();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected = AppsType.values()[position];
        save();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public String getName() {
        return "List Selection";
    }
}
