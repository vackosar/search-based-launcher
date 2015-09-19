package com.vackosar.searchbasedlauncher.boundary;


import android.view.KeyEvent;
import android.widget.ViewAnimator;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;

import java.util.logging.Logger;

import roboguice.activity.RoboActivity;
import roboguice.event.EventManager;
import roboguice.inject.ContentView;

@SuppressWarnings("Convert2Lambda")
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    @Inject private MenuButton menuButton;
    @Inject private AppsView appsView;
    @Inject private EventManager eventManager;
    @Inject private WifiButton wifiButton;
    @Inject private BluetoothButton bluetoothButton;
    @Inject private CameraButton cameraButton;
    @Inject private WikiButton wikiButton;

    public static String APP_PACKAGE_NAME = "com.vackosar.searchbasedlauncher";
    private Logger log = Logger.getLogger(getClass().getSimpleName());

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_MENU) {
            eventManager.fire(new MenuButton.ToggleEvent());
        } else if (keycode == KeyEvent.KEYCODE_SEARCH) {
            startSearch("", false, null, true);
        } else if (keycode == KeyEvent.KEYCODE_BACK) {
            ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
            if (mViewAnimator.getDisplayedChild() == 1) {
                eventManager.fire(new MenuButton.ToggleEvent());
            }
        } else {
            return super.onKeyUp(keycode, event);
        }
        return true;
    }
}