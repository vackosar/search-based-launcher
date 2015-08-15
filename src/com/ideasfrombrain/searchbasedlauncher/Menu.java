package com.ideasfrombrain.searchbasedlauncher;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ViewAnimator;

public class Menu {
    public static final int MENU_CHILD_ID = 1;
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
        toggleView();
        if (! isMenuShown()) {
            mainActivity.getAppsManager().reload();
        } else {
            appTypeSelector.requestFocus();
        }
    }

    private boolean isMenuShown() {
        return getViewAnimator().getDisplayedChild() == MENU_CHILD_ID;
    }

    private void toggleView() {
        ViewAnimator mViewAnimator = getViewAnimator();
        mViewAnimator.showNext();
    }

    private ViewAnimator getViewAnimator() {
        return (ViewAnimator) mainActivity.findViewById(R.id.viewAnimator);
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
