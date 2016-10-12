package com.vackosar.searchbasedlauncher.boundary;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;

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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
    }
}