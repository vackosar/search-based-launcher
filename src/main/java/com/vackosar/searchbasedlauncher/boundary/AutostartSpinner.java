package com.vackosar.searchbasedlauncher.boundary;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AutostartSpinner implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.autostart) Spinner spinner;
    @Inject private SingletonPersister<AutostartSpinner> persister;
    @Expose private boolean autostart = true;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        spinner.setOnItemSelectedListener(this);
        load();
    }

    private void save() {
        persister.save(this);
    }

    private void load() {
        persister.load(this);
        if (autostart) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
        }
    }

    public boolean isOn() {
        return autostart;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            autostart = true;
        } else {
            autostart = false;
        }
        save();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
