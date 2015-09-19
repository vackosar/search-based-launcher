package com.vackosar.searchbasedlauncher;

import android.view.View;
import android.widget.TextView;

public class AutostartButton implements View.OnClickListener {

    private boolean autostart;
    private final TextView textView;
    private ColorService colorService = new ColorService();
    private final PreferencesAdapter preferencesAdapter;
    private final MainActivity mainActivity;

    public AutostartButton (MainActivity mainActivity, PreferencesAdapter preferencesAdapter) {
        this.mainActivity = mainActivity;
        this.preferencesAdapter = preferencesAdapter;
        textView = (TextView) mainActivity.findViewById(R.id.autostartButton);
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
