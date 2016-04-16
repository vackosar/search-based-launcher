package com.vackosar.searchbasedlauncher.entity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.boundary.BluetoothToggle;
import com.vackosar.searchbasedlauncher.boundary.WifiToggle;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AppExecutor {
    @Inject private Activity activity;
    @Inject private WifiToggle wifiToggle;
    @Inject private BluetoothToggle bluetoothToggle;

    public void act(App app) {
        try {
            startActivity(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startActivity(App app) {
        if (wifiToggle.getApp().equals(app)) {
            wifiToggle.act();
        } else if (bluetoothToggle.getApp().equals(app)) {
            bluetoothToggle.act();
        } else {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.setComponent(new ComponentName(app.getName(), app.getActivity()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            activity.startActivity(intent);
        }
    }
}
