package com.vackosar.searchbasedlauncher;

import android.widget.RadioGroup;

public class AppTypeSelector implements RadioGroup.OnCheckedChangeListener {
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;
    final private MainActivity mainActivity;
    private final PreferencesAdapter preferencesAdapter;
    private AppsType selected = DEFAULT_SELECTED;
    private final RadioGroup radioGroup;

    public AppTypeSelector(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        radioGroup = (RadioGroup) mainActivity.findViewById(R.id.appListRadioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        preferencesAdapter = mainActivity.getPreferencesAdapter();
        selected = AppsType.parseOrdinal(load());
    }

    private Integer load() {
        return preferencesAdapter.load(getClass().getName(), Integer.class);
    }

    public void save() {
        preferencesAdapter.save(getClass().getName(), selected.ordinal());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        selected = AppsType.parseViewId(checkedId);
        mainActivity.getMenuButton().toggle();
    }

    public AppsType getSelected() {
        return selected;
    }

    public void requestFocus() {
        radioGroup.requestFocus();
    }
}
