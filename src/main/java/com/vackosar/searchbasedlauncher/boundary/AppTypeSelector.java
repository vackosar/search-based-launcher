package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.RadioGroup;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.AppsType;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnStartEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppTypeSelector implements RadioGroup.OnClickListener {

    @InjectView(R.id.appListRadioGroup) RadioGroup radioGroup;
    @Inject private EventManager eventManager;
    @Inject private Activity activity;
    @Inject private SingletonPersister<AppTypeSelector> persister;

    @Expose private AppsType selected = DEFAULT_SELECTED;
    public static final AppsType DEFAULT_SELECTED = AppsType.normal;

    @SuppressWarnings("unused")
    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        for (AppsType appsType: AppsType.values()) {
            final View view = activity.findViewById(appsType.getViewId());
            view.setOnClickListener(this);
        }
    }

    @SuppressWarnings("unused")
    public void onStartEvent(@Observes OnStartEvent onStartEvent) {
        load();
    }

    private void load() {
        persister.load(this);
        radioGroup.check(selected.getViewId());
    }

    public void save() {
        persister.save(this);
    }

    public AppsType getSelected() {
        return selected;
    }

    @Override
    public void onClick(View v) {
        selected = AppsType.parseViewId(radioGroup.getCheckedRadioButtonId());
        save();
        eventManager.fire(new MenuButton.ToggleEvent());
    }
}
