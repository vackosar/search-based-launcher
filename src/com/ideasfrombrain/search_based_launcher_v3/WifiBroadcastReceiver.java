package com.ideasfrombrain.search_based_launcher_v3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;

public class WifiBroadcastReceiver extends BroadcastReceiver {


    private final WifiManager wifiManager;
    private final TextView wifiButton;

    public WifiBroadcastReceiver(WifiManager wifiManager, TextView wifiButton) {
        this.wifiManager = wifiManager;
        this.wifiButton = wifiButton;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEBUG", "recieved Broadcast");
        if(wifiManager !=null) {
            if(wifiManager.isWifiEnabled()) {
                wifiButton.setBackgroundColor(-16711936);
            }
            else {
                wifiButton.setBackgroundColor(-12303292);
            }
        }
    }
}
