package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;

import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnStartEvent;
import roboguice.event.Observes;

public class WifiToggle extends RoboActivity {

    @Inject private WifiButton wifiButton;
    @Inject private Activity activity;

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
    }

    public void onStartEvent(@Observes OnStartEvent onStartEvent) {
        toggle();
        this.finish();
    }

    private WifiManager wifiManager;

    public void toggle() {
        if (isAvailable()) {
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
        }
    }

    private boolean isAvailable () {
        return wifiManager == null;
    }

}
