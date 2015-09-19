package com.vackosar.searchbasedlauncher;

import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;

import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AutostartButton implements View.OnClickListener {

    private boolean autostart;
    @InjectView(R.id.autostartButton) TextView textView;
    private final ColorService colorService = new ColorService();
    @Inject PreferencesAdapter preferencesAdapter;
    @Inject MainActivity mainActivity;

    public AutostartButton () {
        textView.setOnClickListener(this);
        load();
    }

    @Override
    public void onClick(View v) {
        autostart=!autostart;
        setColor();
    }

    private void setColor() {
        colorService.setActive(autostart, textView);
    }

    public boolean isOn() {
        return autostart;
    }

    public void save() {
        preferencesAdapter.save(getClass().getName(), new Boolean(autostart));
    }

    public void load() {
        autostart = preferencesAdapter.load(getClass().getName(), Boolean.class);
        setColor();
    }
}
