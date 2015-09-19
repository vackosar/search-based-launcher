package com.vackosar.searchbasedlauncher;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

public class CameraButton extends Colorful implements View.OnClickListener {
    @Inject private Activity activity;
    @InjectView(R.id.cameraButton) private TextView textView;
    private boolean hasCam;
    private final ColorService colorService = new ColorService();

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        PackageManager pm = activity.getPackageManager();
        hasCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!(hasCam)) {
            colorService.setInvisible(textView);
        }
        textView.setOnClickListener(this);
    }

    public void setVisibleIfAvailable () {
        if (hasCam) {
            colorService.setInvisible(textView);
        }
    }

    @Override
    public void onClick(View v) {
        activity.startActivity((new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)));
    }

    @Override
    View getView() {
        return textView;
    }
}
