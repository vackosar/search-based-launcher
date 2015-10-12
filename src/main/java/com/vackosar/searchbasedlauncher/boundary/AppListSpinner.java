package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.AppsType;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppListSpinner implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.appList) Spinner spinner;
    @Inject private EventManager eventManager;
    @Inject private Activity activity;
    @Inject private SingletonPersister<AppListSpinner> persister;

    public static final AppsType DEFAULT_SELECTED = AppsType.normal;

    @Expose private AppsType selected = DEFAULT_SELECTED;

    @SuppressWarnings("unused")
    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        spinner.setOnItemSelectedListener(this);
        load();
    }

    private void load() {
        persister.load(this);
        spinner.setSelection(selected.ordinal());
    }

    public void save() {
        persister.save(this);
    }

    public AppsType getSelected() {
        return selected;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected = AppsType.values()[position];
        save();
        eventManager.fire(new MenuButton.ToggleEvent());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
