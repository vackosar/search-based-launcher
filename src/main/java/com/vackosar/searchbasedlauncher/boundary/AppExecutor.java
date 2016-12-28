package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.App;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AppExecutor {
    @Inject private Activity activity;
    @Inject private WifiToggle wifiToggle;
    @Inject private BluetoothToggle bluetoothToggle;
    @Inject private SearchText searchText;

    public void act(App app) {
        try {
            startActivity(app);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            searchText.clearText();
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
            intent.setFlags(Intent.	FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivity(intent);
        }
    }
}
