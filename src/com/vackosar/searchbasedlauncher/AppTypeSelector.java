package com.vackosar.searchbasedlauncher;

import android.content.SharedPreferences;
import android.widget.RadioGroup;

public class AppTypeSelector implements RadioGroup.OnCheckedChangeListener {
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;
    final private MainActivity mainActivity;
    private AppsType selected = DEFAULT_SELECTED;
    private final RadioGroup radioGroup;
    private final SharedPreferences sharedPreferences;

    public AppTypeSelector(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        radioGroup = (RadioGroup) mainActivity.findViewById(R.id.appListRadioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        sharedPreferences = mainActivity.getApplicationContext().getSharedPreferences("preferencename", 0);
        selected = AppsType.parseOrdinal(sharedPreferences.getInt("selected", 0));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        selected = AppsType.parseViewId(checkedId);
        mainActivity.getMenuButton().toggle();
    }

    public AppsType getSelected() {
        return selected;
    }

    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selected", selected.ordinal());
        editor.commit();
    }

    public void requestFocus() {
        radioGroup.requestFocus();
    }
}
