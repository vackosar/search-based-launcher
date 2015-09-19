package com.vackosar.searchbasedlauncher;

import android.bluetooth.BluetoothAdapter;
import android.view.View;
import android.widget.TextView;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

public class BluetoothButton extends Colorful implements View.OnClickListener {
    @InjectView(R.id.bluetoothButton) private TextView textView;

    final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public void onCreate(@Observes OnCreateEvent onCreate) {
        textView.setOnClickListener(this);
    }

    public void setVisibleIfAvailable () {
        if (bluetoothAdapter == null) {
            setInvisible();
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

    @Override
    View getView() {
        return textView;
    }
}

