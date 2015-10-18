package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ThemeSelector extends SelectAction<ThemeSelector.Theme> implements Indentifiable {

    @Inject private Activity activity;
    @Expose private ThemeSelector.Theme theme;

    private Theme DEFAULT_THEME = Theme.Wallpaper;

    @SuppressWarnings("unused")
    public ThemeSelector() { theme = DEFAULT_THEME; }

    public ThemeSelector(Activity activity) {
        super();
        theme = DEFAULT_THEME;
        this.activity = activity;
        setActivity(activity);
    }

    @Override
    protected void load() {
        super.load();
        activity.setTheme(theme.getId());
    }

    @Override
    protected Enum<Theme> getSelected() {
        return theme;
    }

    @Override
    public void setSelected(Enum<Theme> selected) {
        this.theme = (Theme) selected;
        save();
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        load();
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Theme";
    }

    public enum Theme {
        Wallpaper(R.style.Wallpaper),
        Black(R.style.Black),
        White(R.style.White);

        private int id;

        Theme(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
