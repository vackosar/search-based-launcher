package com.vackosar.searchbasedlauncher.boundary;

import android.view.View;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.Colorful;
import com.vackosar.searchbasedlauncher.entity.PreferencesAdapter;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;
import com.vackosar.searchbasedlauncher.entity.SingletonPersisterFactory;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AutostartButton extends Colorful implements View.OnClickListener {

    @InjectView(R.id.autostartButton) TextView textView;
    @Inject private PreferencesAdapter preferencesAdapter;
    @Inject private SingletonPersisterFactory singletonPersisterFactory;
    @Expose private boolean autostart = true;
    private SingletonPersister<AutostartButton> persister;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        textView.setOnClickListener(this);
        persister = singletonPersisterFactory.create(this);
        load();
    }

    private void save() {
        persister.save();
        setColor();
    }

    private void load() {
        persister.load();
        setColor();
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    private void toggle() {
        autostart=!autostart;
        save();
    }

    private void setColor() {
        setActivatedColor(autostart);
    }

    public boolean isOn() {
        return autostart;
    }

    @Override
    protected View getView() {
        return textView;
    }
}
