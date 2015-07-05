package com.ideasfrombrain.search_based_launcher_v3;

import android.bluetooth.BluetoothAdapter;
import android.view.View;

public class BluetoothListener implements View.OnClickListener {
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onClick(View v) {
        if ((mBluetoothAdapter != null) && (!mBluetoothAdapter.isEnabled())) {
            mBluetoothAdapter.enable();
        } else if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
    }
}

