package com.vackosar.searchbasedlauncher;

import android.widget.RadioGroup;

public class AppTypeSelector implements RadioGroup.OnCheckedChangeListener {
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;
    final private MainActivity mainActivity;
    private final PreferencesAdapter preferencesAdapter;
    private AppsType selected = DEFAULT_SELECTED;
    private final RadioGroup radioGroup;
    private final MenuButton menuButton;

    public AppTypeSelector(MainActivity mainActivity, PreferencesAdapter preferencesAdapter, MenuButton menuButton) {
        this.mainActivity = mainActivity;
        this.preferencesAdapter = preferencesAdapter;
        this.menuButton = menuButton;
        radioGroup = (RadioGroup) mainActivity.findViewById(R.id.appListRadioGroup);
        radioGroup.setOnCheckedChangeListener(this);
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
        menuButton.toggle();
    }

    public AppsType getSelected() {
        return selected;
    }

    public void requestFocus() {
        radioGroup.requestFocus();
    }
}
