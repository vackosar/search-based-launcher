package com.ideasfrombrain.search_based_launcher_v3;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

public class CameraButton implements View.OnClickListener {
    private final MainActivity mainActivity;
    private final boolean hasCam;
    private final TextView textView;
    private final ColorService colorService = new ColorService();

    public CameraButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.button6);
        PackageManager pm = mainActivity.getPackageManager();
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
        mainActivity.startActivity((new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)));
    }
}
