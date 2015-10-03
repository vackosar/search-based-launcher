package com.vackosar.searchbasedlauncher.boundary;

import android.view.View;
import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.Colorful;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AutostartButton extends Colorful implements View.OnClickListener {

    @InjectView(R.id.autostartButton) TextView textView;
    @Inject private SingletonPersister<AutostartButton> persister;
    @Expose private boolean autostart = true;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        textView.setOnClickListener(this);
        load();
    }

    private void save() {
        persister.save(this);
        setColor();
    }

    private void load() {
        persister.load(this);
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
    protected TextView getView() {
        return textView;
    }
}
