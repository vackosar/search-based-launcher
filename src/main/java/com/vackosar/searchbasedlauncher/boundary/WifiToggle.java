package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.App;

import roboguice.activity.RoboActivity;
import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;

public class WifiToggle extends RoboActivity {

    private static final String NICK = "Wifi Toggle";
    private static final String MSG_PREFIX = "Turned wifi ";
    private static final String OFF = "off";
    private static final String ON = "on";
    private static final String SUFFIX = ".";

    @Inject private Activity activity;
    @Inject private Context context;

    private WifiManager wifiManager;
    private App app;

    private void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        app = new App(activity.getPackageName(), NICK, getClass().getName());
    }

    public void toggle() {
        if (isAvailable()) {
            final boolean enabled = !wifiManager.isWifiEnabled();
            wifiManager.setWifiEnabled(enabled);
            final String msg = createMsg(enabled);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    private String createMsg(boolean enabled) {
        return MSG_PREFIX + (enabled ? ON : OFF) + SUFFIX;
    }

    private boolean isAvailable () {
        return wifiManager != null;
    }

    public App getApp() {
        return app;
    }
}
