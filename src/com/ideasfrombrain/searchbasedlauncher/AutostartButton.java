package com.ideasfrombrain.searchbasedlauncher;

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
        autostart = false;
        onClick(null);
    }

    @Override
    public void onClick(View v) {
        autostart=!autostart;
        colorService.setActive(autostart, textView);
    }

    public boolean isOn() {
        return autostart;
    }
}
