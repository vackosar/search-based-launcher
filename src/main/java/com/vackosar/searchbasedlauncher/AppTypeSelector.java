package com.vackosar.searchbasedlauncher;

import android.widget.RadioGroup;

import com.google.inject.Inject;

import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppTypeSelector implements RadioGroup.OnCheckedChangeListener {
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;
    private AppsType selected = DEFAULT_SELECTED;

    @InjectView(R.id.appListRadioGroup) RadioGroup radioGroup;
    @Inject MainActivity mainActivity;
//    @Inject
    MenuButton menuButton;
    @Inject PreferencesAdapter preferencesAdapter;

    public AppTypeSelector() {
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
