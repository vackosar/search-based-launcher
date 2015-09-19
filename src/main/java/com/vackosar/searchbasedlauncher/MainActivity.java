package com.vackosar.searchbasedlauncher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ViewAnimator;

import com.google.inject.Inject;

import java.util.logging.Logger;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@SuppressWarnings("Convert2Lambda")
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {
    public static String APP_PACKAGE_NAME = "com.vackosar.searchbasedlauncher";

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    private final BroadcastReceiver mPkgApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            appsManager.load();
        }
    };
    MenuButton menuButton;

    @Inject AppsView appsView;
    private WifiButton wifiButton;
    private BluetoothButton bluetoothButton;
    private CameraButton cameraButton;
    private WikiButton wikiButton;

    private void registerIntentReceivers() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(mPkgApplicationsReceiver, filter);
    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_MENU) {
            menuButton.toggle();
        } else if (keycode == KeyEvent.KEYCODE_SEARCH) {
            startSearch("", false, null, true);
        } else if (keycode == KeyEvent.KEYCODE_BACK) {
            ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
            if (mViewAnimator.getDisplayedChild() == 1) {
                menuButton.toggle();
            }
        } else {
            return super.onKeyUp(keycode, event);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerIntentReceivers();
        initializeViewWrappers();
//        eventManager.fire();
    }

    private void initializeViewWrappers() {
        wifiButton = new WifiButton(this);
        bluetoothButton = new BluetoothButton(this);
        cameraButton = new CameraButton(this);
        wikiButton = new WikiButton(this);
    }

    @Override
    public void onDestroy() {
        wifiButton.unregisterReceiver();
        unregisterReceiver(mPkgApplicationsReceiver);
        super.onDestroy();
    }
}