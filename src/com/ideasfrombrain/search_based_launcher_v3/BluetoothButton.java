package com.ideasfrombrain.search_based_launcher_v3;

import android.bluetooth.BluetoothAdapter;
import android.view.View;
import android.widget.TextView;

public class BluetoothButton implements View.OnClickListener {
    final MainActivity mainActivity;
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    final ColorService colorService = new ColorService();
    private final TextView textView;

    public BluetoothButton (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.bluetoothButton);
        textView.setOnClickListener(this);
    }

    public void setVisibleIfAvailable () {
        if (mBluetoothAdapter == null) {
            colorService.setInvisible(textView);
        }
    }

    @Override
    public void onClick(View v) {
        if ((mBluetoothAdapter != null) && (!mBluetoothAdapter.isEnabled())) {
            mBluetoothAdapter.enable();
        } else if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
    }
}

