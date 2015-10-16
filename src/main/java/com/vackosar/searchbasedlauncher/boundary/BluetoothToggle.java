package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.App;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class BluetoothToggle {

    private static final String MSG_PREFIX = "Turned bluetooth ";
    private static final String OFF = "off";
    private static final String ON = "on";
    private static final String SUFFIX = ".";
    private static final String NICK = "Bluetooth Toggle";
    private static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    @Inject private Activity activity;
    @Inject private Context context;

    private App app;

    @SuppressWarnings("unused")
    private void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        app = new App(activity.getPackageName(), NICK, getClass().getName());
    }

    public boolean isAvailable() {
        return bluetoothAdapter != null;
    }

    public void act() {
        if (isAvailable()) {
            final boolean enabled = bluetoothAdapter.isEnabled();
            if (enabled) {
                bluetoothAdapter.disable();
            } else {
                bluetoothAdapter.enable();
            }
            final String msg = createMsg(! enabled);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    private String createMsg(boolean enabled) {
        return MSG_PREFIX + (enabled ? ON : OFF) + SUFFIX;
    }

    public App getApp() {
        return app;
    }
}

