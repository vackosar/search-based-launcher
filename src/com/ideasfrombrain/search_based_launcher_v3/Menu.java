package com.ideasfrombrain.search_based_launcher_v3;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ViewAnimator;

public class Menu {
    private final MainActivity mainActivity;
    private final AppTypeSelector appTypeSelector;
    private final WifiButton wifiButton;
    private final BluetoothButton bluetoothButton;
    private final CameraButton cameraButton;

    public Menu(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        appTypeSelector = new AppTypeSelector(mainActivity);
        wifiButton = new WifiButton(mainActivity);
        bluetoothButton = new BluetoothButton(mainActivity);
        cameraButton = new CameraButton(mainActivity);
        createMenuDonateButton();
    }

    public void toggle(Boolean doLoadApps) {
        mainActivity.getSearchText().setNormalColor();
        ViewAnimator mViewAnimator = (ViewAnimator) mainActivity.findViewById(R.id.viewAnimator);
        mViewAnimator.showNext();

        if (mViewAnimator.getDisplayedChild() == 0) {
            if (doLoadApps) {
                mainActivity.getAppListManager().reload();
            }

            if (appTypeSelector.getSelected() != AppsType.normal) {
                mainActivity.getSearchText().setSpaceCharacterToText();
                appTypeSelector.setInvisible();
            } else {
                wifiButton.setVisibleIfAvailable();
                bluetoothButton.setVisibleIfAvailable();
                cameraButton.setVisibleIfAvailable();
                mainActivity.getSearchText().clearText();
            }

        } else {
            RadioGroup mRadioGroup = (RadioGroup) mainActivity.findViewById(R.id.appListRadioGroup);
            mRadioGroup.requestFocus();
        }
    }

    private void createMenuDonateButton() {
        mainActivity.findViewById(R.id.donateButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mainActivity.APP_PACKAGE_NAME));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mainActivity.startActivity(marketIntent);
            }
        });
    }

    public AppTypeSelector getAppTypeSelector() {
        return appTypeSelector;
    }

    public void onDestroy() {
        wifiButton.unregisterReceiver();
    }
}
