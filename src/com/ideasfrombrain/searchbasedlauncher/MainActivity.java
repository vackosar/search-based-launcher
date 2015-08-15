package com.ideasfrombrain.searchbasedlauncher;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ViewAnimator;


@SuppressWarnings("Convert2Lambda")
public class MainActivity extends Activity {
    public static String APP_PACKAGE_NAME = "com.ideasfrombrain.searchbasedlauncher";

    boolean newerAndroidVersion = true;

    private final BroadcastReceiver mPkgApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            appsManager.load();
        }
    };
    private SearchText searchText;
    private AutostartButton autostartButton;
    private AppsManager appsManager;
    private Menu menu;
    private AppsView appsView;

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
            menu.toggle(false);
        } else if (keycode == KeyEvent.KEYCODE_SEARCH) {
            startSearch("", false, null, true);
        } else if (keycode == KeyEvent.KEYCODE_BACK) {
            ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
            if (mViewAnimator.getDisplayedChild() == 1) {
                menu.toggle(false);
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
        registerIntentReceivers();
        appsView = new AppsView(this);
        menu = new Menu(this);
        autostartButton = new AutostartButton(this);
        setAndroidVersion();
        appsManager = new AppsManager(this);
        searchText = new SearchText(this);
        appsManager.loadFromSavedState(savedInstanceState);
    }

    private void setAndroidVersion() {
        String Aversion = android.os.Build.VERSION.RELEASE;
        newerAndroidVersion = !(Aversion.startsWith("1.") ||
                Aversion.startsWith("2.0") ||
                Aversion.startsWith("2.1"));
    }

    public SearchText getSearchText() {
        return searchText;
    }

    public AppsManager getAppsManager() {
        return appsManager;
    }

    public AutostartButton getAutostartButton() {
        return autostartButton;
    }

    public AppsView getAppsView() {
        return appsView;
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        menu.getAppTypeSelector().save();
        appsManager.saveState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchText.setNormalColor();
        if ((menu.getAppTypeSelector().getSelected() == AppsType.normal) && autostartButton.isOn()) {
            searchText.clearText();
        }
    }

    @Override
    public void onDestroy() {
        menu.onDestroy();
        unregisterReceiver(mPkgApplicationsReceiver);
        super.onDestroy();
    }
}