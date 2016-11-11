package com.vackosar.searchbasedlauncher.boundary;


import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.CrashReporter;
import com.vackosar.searchbasedlauncher.control.CustomExceptionHandler;

import roboguice.activity.RoboActivity;
import roboguice.event.EventManager;
import roboguice.inject.ContentView;

@SuppressWarnings("Convert2Lambda")
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    public static final String INITIAL_SEARCH_QUERY = "";
    @Inject private MenuButton menuButton;
    @Inject private AppsView appsView;
    @Inject private EventManager eventManager;
    @Inject private MenuList menuList;
    private CrashReporter crashReporter;

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_BACK:
                if (eventManager != null){
                    eventManager.fire(new MenuButton.ToggleEvent());
                }
                break;
            case KeyEvent.KEYCODE_SEARCH:
                startSearch(INITIAL_SEARCH_QUERY, false, null, true);
                break;
            default:
                return super.onKeyUp(keycode, event);
        }
        return true;
    }

    /** Set theme before view rendering. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ThemeSelector(this);
        super.onCreate(savedInstanceState);
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this.getApplicationContext().getFilesDir().getPath()));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        crashReporter = new CrashReporter();
        crashReporter.reportPreviousCrashes(this);

    }
}