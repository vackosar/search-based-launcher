package com.vackosar.searchbasedlauncher.boundary;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.Colorful;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

public class CameraButton extends Colorful implements View.OnClickListener {

    @Inject private Activity activity;
    @InjectView(R.id.cameraButton) private TextView textView;

    private boolean hasCam;

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        PackageManager pm = activity.getPackageManager();
        hasCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!(hasCam)) {
            setInvisible();
        }
        textView.setOnClickListener(this);
    }

    public void setVisibleIfAvailable () {
        if (hasCam) {
            setInvisible();
        }
    }

    @Override
    public void onClick(View v) {
        activity.startActivity((new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)));
    }

    @Override
    protected View getView() {
        return textView;
    }
}
