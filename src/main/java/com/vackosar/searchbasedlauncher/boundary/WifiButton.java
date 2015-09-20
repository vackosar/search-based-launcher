package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.Colorful;

import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

public class WifiButton extends Colorful implements View.OnClickListener {

    @InjectView(R.id.wifiButton) private TextView textView;
    @Inject private Activity activity;

    private WifiManager wifiManager;

    private BroadcastReceiver RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d("DEBUG", "recieved Broadcast");
            if (wifiManager != null) {
                if (wifiManager.isWifiEnabled()) {
                    setActivatedColor();
                } else {
                    setNormalColor();
                }
            }
        }
    };

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
            setInvisible();
        } else {
            setVisible();
        }
    }

    @Override
    public View getView() {
        return textView;
    }

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        textView = (TextView) activity.findViewById(R.id.wifiButton);
        wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        setVisibleIfAvailable();
        if (isAvailable()) {
            if(wifiManager.isWifiEnabled()) {
                setActivatedColor();
            }
            else {
                setNormalColor();
            }
        }
        textView.setOnClickListener(this);
        IntentFilter filter = new IntentFilter( );
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        activity.registerReceiver(RECEIVER, filter);
    }

    public void onDestroy(@Observes OnDestroyEvent onDestroyEvent) {
        activity.unregisterReceiver(RECEIVER);
    }
}
