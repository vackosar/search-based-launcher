package com.vackosar.searchbasedlauncher;


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
    public static String APP_PACKAGE_NAME = "com.vackosar.searchbasedlauncher";

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
    private MenuButton menuButton;
    private AppsView appsView;
    private WifiButton wifiButton;
    private BluetoothButton bluetoothButton;
    private CameraButton cameraButton;
    private AppTypeSelector appTypeSelector;
    private DonateButton donateButton;

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
        registerIntentReceivers();
        appsView = new AppsView(this);
        menuButton = new MenuButton(this);
        autostartButton = new AutostartButton(this);
        wifiButton = new WifiButton(this);
        bluetoothButton = new BluetoothButton(this);
        cameraButton = new CameraButton(this);
        setAndroidVersion();
        appsManager = new AppsManager(this);
        searchText = new SearchText(this);
        appTypeSelector = new AppTypeSelector(this);
        donateButton = new DonateButton(this);
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

    public MenuButton getMenuButton() {
        return menuButton;
    }

    public AppTypeSelector getAppTypeSelector() {
        return appTypeSelector;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        getAppTypeSelector().save();
        appsManager.saveState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchText.setNormalColor();
        if ((getAppTypeSelector().getSelected() == AppsType.normal) && autostartButton.isOn()) {
            searchText.clearText();
        }
    }

    @Override
    public void onDestroy() {
        wifiButton.unregisterReceiver();
        unregisterReceiver(mPkgApplicationsReceiver);
        super.onDestroy();
    }
}