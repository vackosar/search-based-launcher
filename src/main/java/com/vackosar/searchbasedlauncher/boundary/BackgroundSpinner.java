package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class BackgroundSpinner implements AdapterView.OnItemSelectedListener, Indentifiable {

    @InjectView(R.id.background) private Spinner spinner;
    @Inject private Activity activity;
    @Inject private SingletonPersister<BackgroundSpinner> persister;

    private Theme DEFAULT_THEME = Theme.wallpaper;

    @Expose private Theme theme;
    private boolean firstSkipped;

    public BackgroundSpinner() {}

    public BackgroundSpinner(Activity activity) {
        this.activity = activity;
        this.persister = new SingletonPersister<>(activity.getApplicationContext());
        load();
    }

    private void load() {
        persister.load(this);
        if (theme == null) {
            theme = DEFAULT_THEME;
        }
        activity.setTheme(theme.getId());
    }

    private void sync() {
        spinner.setSelection(theme.ordinal());
    }

    private void save() {
        persister.save(this);
    }

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        spinner.setOnItemSelectedListener(this);
        firstSkipped = false;
        load();
        sync();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (firstSkipped) {
            this.theme = Theme.values()[position];
            save();
            activity.finish();
            activity.startActivity(new Intent(activity, activity.getClass()));
        } else {
            firstSkipped = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private enum Theme {
        wallpaper(R.style.Wallpaper),
        black(R.style.Black),
        white(R.style.White);

        private int id;

        Theme(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
