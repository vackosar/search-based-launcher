package com.ideasfrombrain.search_based_launcher_v3;

import android.content.SharedPreferences;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AppTypeSelector implements RadioGroup.OnCheckedChangeListener {
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;
    final private MainActivity mainActivity;
    private AppsType selected = DEFAULT_SELECTED;
    private final RadioGroup radioGroup;
    private final SharedPreferences sharedPreferences;
    private final ColorService colorService = new ColorService();
    private final TextView flashButton;
    private final TextView wifiButton;
    private final TextView button3;
    private final TextView bluetoothButton;

    public AppTypeSelector(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        radioGroup = (RadioGroup) mainActivity.findViewById(R.id.appListRadioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        sharedPreferences = mainActivity.getApplicationContext().getSharedPreferences("preferencename", 0);
        selected = AppsType.of(sharedPreferences.getInt("selected", 0));
        flashButton = (TextView) mainActivity.findViewById(R.id.flashButton);
        wifiButton = (TextView) mainActivity.findViewById(R.id.wifiButton);
        button3 = (TextView) mainActivity.findViewById(R.id.bluetoothButton);
        bluetoothButton = (TextView) mainActivity.findViewById(R.id.cameraButton);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        selected = AppsType.of(checkedId);
        mainActivity.getMenu().toggle(true);
    }

    public void setInvisible() {
        colorService.setInvisible(flashButton);
        colorService.setInvisible(wifiButton);
        colorService.setInvisible(button3);
        colorService.setInvisible(bluetoothButton);
    }

    public AppsType getSelected() {
        return selected;
    }

    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selected", selected.ordinal());
        editor.commit();
    }
}
