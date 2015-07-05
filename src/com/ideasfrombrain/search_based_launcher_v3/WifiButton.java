package com.ideasfrombrain.search_based_launcher_v3;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.TextView;

public class WifiButton implements View.OnClickListener, Colorful {
    final MainActivity mainActivity;
    final ColorService colorService = new ColorService();
    private final WifiManager wifiManager;
    private final TextView textView;

    public WifiButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.button2);
        wifiManager = (WifiManager) mainActivity.getSystemService(Context.WIFI_SERVICE);
        setVisibleIfAvailable();
        if (isAvailable()) {
            if(wifiManager.isWifiEnabled()) {
                colorService.setActive(textView);
            }
            else {
                colorService.setNormal(textView);
            }
        }
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(!wifiManager.isWifiEnabled());
        }
    }

    private boolean isAvailable () {
        return wifiManager == null;
    }

    public void setVisibleIfAvailable () {
        if (wifiManager == null) {
            colorService.setInvisible(textView);
        } else {
            colorService.setVisible(textView);
        }
    }

    @Override
    public View getView() {
        return textView;
    }

}
