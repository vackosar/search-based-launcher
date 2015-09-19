package com.vackosar.searchbasedlauncher;

import android.bluetooth.BluetoothAdapter;
import android.view.View;
import android.widget.TextView;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

public class BluetoothButton implements View.OnClickListener {
    final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    final ColorService colorService = new ColorService();
    @InjectView(R.id.bluetoothButton) private TextView textView;

    public void onCreate(@Observes OnCreateEvent onCreate) {
        textView.setOnClickListener(this);
    }

    public void setVisibleIfAvailable () {
        if (bluetoothAdapter == null) {
            colorService.setInvisible(textView);
        }
    }

    @Override
    public void onClick(View v) {
        if ((bluetoothAdapter != null) && (!bluetoothAdapter.isEnabled())) {
            bluetoothAdapter.enable();
        } else if (bluetoothAdapter != null) {
            bluetoothAdapter.disable();
        }
    }
}

