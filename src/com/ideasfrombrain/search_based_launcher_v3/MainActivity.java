package com.ideasfrombrain.search_based_launcher_v3;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ViewAnimator;


@SuppressWarnings("Convert2Lambda")
public class MainActivity extends Activity {
    public static String APP_PACKAGE_NAME = "com.ideasfrombrain.search_based_launcher_v3";

    boolean newerAndroidVersion = true;

    private final BroadcastReceiver mPkgApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            appsManager.reload();
        }
    };
    private SearchText searchText;
    private AutostartButton autostartButton;
    private WifiButton wifiButton;
    private BluetoothButton bluetoothButton;
    private CameraButton cameraButton;
    private RadioButtons radioButtons;
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
            showNext(false);
        } else if (keycode == KeyEvent.KEYCODE_SEARCH) {
            startSearch("", false, null, true);
        } else if (keycode == KeyEvent.KEYCODE_BACK) {
            ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
            if (mViewAnimator.getDisplayedChild() == 1) {
                showNext(false);
            }
        } else {
            return super.onKeyUp(keycode, event);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appsManager = new AppsManager(this, savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = new SearchText(this);
        autostartButton = new AutostartButton(this);
        wifiButton = new WifiButton(this);
        bluetoothButton = new BluetoothButton(this);
        cameraButton = new CameraButton(this);
        createMenuDonateButton();
        radioButtons = new RadioButtons(this);
        setAndroidVersion();
        registerIntentReceivers();
    }

    private void createMenuDonateButton() {
        findViewById(R.id.donateButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(marketIntent);
            }
        });
    }

    private void setAndroidVersion() {
        String Aversion = android.os.Build.VERSION.RELEASE;
        newerAndroidVersion = !(Aversion.startsWith("1.") ||
                Aversion.startsWith("2.0") ||
                Aversion.startsWith("2.1"));
    }

    public RadioButtons getRadioButtons() {
        return radioButtons;
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

    public void showNext(Boolean doLoadApps) {
        searchText.setNormalColor();
        ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
        mViewAnimator.showNext();

        if (mViewAnimator.getDisplayedChild() == 0) {
            if (doLoadApps) {
                appsManager.reload();
            }

            if (radioButtons.getCheckedRadioButton() > 0) {
                searchText.setSpaceCharacterToText();
                radioButtons.setInvisible();
            } else {
                wifiButton.setVisibleIfAvailable();
                bluetoothButton.setVisibleIfAvailable();
                cameraButton.setVisibleIfAvailable();
                searchText.clearText();
            }

        } else {
            RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
            mRadioGroup.requestFocus();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        radioButtons.save();
        appsManager.saveState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchText.setNormalColor();
        if ((radioButtons.getCheckedRadioButton() == 0) && autostartButton.isOn()) {
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