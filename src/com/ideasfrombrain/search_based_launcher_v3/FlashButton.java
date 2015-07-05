package com.ideasfrombrain.search_based_launcher_v3;

import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

public class FlashButton implements View.OnClickListener {
    final ColorService colorService = new ColorService();
    final MainActivity mainActivity;
    final boolean hasCam;

    public FlashButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        final TextView textView = (TextView) mainActivity.findViewById(R.id.button1);
        PackageManager pm = mainActivity.getPackageManager();
        hasCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

        colorService.setInvisible(textView);
        if (!(hasCam)) {
            colorService.setInvisible(textView);
        }
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        //Camera mCamera;
//        String value;
//        if (isFlashOn) // we are being ask to turn it on
//        {
//            value = Camera.Parameters.FLASH_MODE_TORCH;
//        } else  // we are being asked to turn it off
//        {
//            value = Camera.Parameters.FLASH_MODE_AUTO;
//        }
//
//
//        try {
//
//            Camera cam = Camera.open();
//            Parameters p = cam.getParameters();
//            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
//            cam.setParameters(p);
//            cam.startPreview();
//
//            String nowMode = p.getFlashMode();
//
//            if (isFlashOn && nowMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
//                isFlashOn = true;
//            }
//            if (!isFlashOn && nowMode.equals(Camera.Parameters.FLASH_MODE_AUTO)) {
//                isFlashOn = true;
//            }
//            isFlashOn = false;
//        } catch (Exception ex) {
//            // nothing to do
//            Log.d("DEBUG", "error");
//        }
    }
}
