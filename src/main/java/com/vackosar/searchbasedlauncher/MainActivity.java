package com.vackosar.searchbasedlauncher;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ViewAnimator;

import java.util.logging.Logger;

import dagger.ObjectGraph;


@SuppressWarnings("Convert2Lambda")
public class MainActivity extends Activity {
    public static String APP_PACKAGE_NAME = "com.vackosar.searchbasedlauncher";

    private Logger log = Logger.getLogger(getClass().getSimpleName());

    private final BroadcastReceiver mPkgApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            appsManager.load();
        }
    };
    private MenuButton menuButton;
    private WifiButton wifiButton;
    private BluetoothButton bluetoothButton;
    private CameraButton cameraButton;
    private WikiButton wikiButton;
    private AppsManager appsManager;

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
        setContentView(R.layout.activity_main);
        ObjectGraph objectGraph = ObjectGraph.create(new Module(this));
        appsManager = objectGraph.get(AppsManager.class);
        menuButton = objectGraph.get(MenuButton.class);
        objectGraph.get(AppsView.class);
        registerIntentReceivers();
        initializeViewWrappers();
        appsManager.load();
    }

    private void initializeViewWrappers() {
        wifiButton = new WifiButton(this);
        bluetoothButton = new BluetoothButton(this);
        cameraButton = new CameraButton(this);
        wikiButton = new WikiButton(this);
    }

    public MenuButton getMenuButton() {
        return menuButton;
    }

    @Override
    public void onDestroy() {
        wifiButton.unregisterReceiver();
        unregisterReceiver(mPkgApplicationsReceiver);
        super.onDestroy();
    }
}