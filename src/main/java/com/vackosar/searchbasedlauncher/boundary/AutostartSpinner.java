package com.vackosar.searchbasedlauncher.boundary;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.YesNo;
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
        spinner.setSelection(YesNo.valueOf(autostart).position);
    }

    public boolean isOn() {
        return autostart;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        autostart = YesNo.valueOf(position).bool;
        save();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
