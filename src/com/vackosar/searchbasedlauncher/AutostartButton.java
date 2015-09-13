package com.vackosar.searchbasedlauncher;

import android.view.View;
import android.widget.TextView;

public class AutostartButton implements View.OnClickListener {

    private final MainActivity mainActivity;
    private boolean autostart;
    private final TextView textView;
    private ColorService colorService = new ColorService();

    public AutostartButton (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
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
        mainActivity.getPreferencesAdapter().save(getClass().getName(), new Boolean(autostart));
    }

    public void load() {
        autostart = mainActivity.getPreferencesAdapter().load(getClass().getName(), Boolean.class);
        setColor();
    }
}
