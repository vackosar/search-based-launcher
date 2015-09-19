package com.vackosar.searchbasedlauncher;

import android.widget.RadioGroup;

import com.google.inject.Inject;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppTypeSelector implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.appListRadioGroup) RadioGroup radioGroup;
    @Inject private PreferencesAdapter preferencesAdapter;
    @Inject private EventManager eventManager;

    private AppsType selected = DEFAULT_SELECTED;
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;

    public void onCreate(@Observes OnCreateEvent onCreate) {
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
        eventManager.fire(new MenuButton.ToggleEvent());
    }

    public AppsType getSelected() {
        return selected;
    }

    public void requestFocus() {
        radioGroup.requestFocus();
    }
}
