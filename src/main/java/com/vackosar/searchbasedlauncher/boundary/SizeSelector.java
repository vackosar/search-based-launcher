package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.FontSize;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class SizeSelector extends SelectAction<FontSize> {

    public static final FontSize DEFAULT = FontSize.pt9;

    @Inject private Activity activity;
    @Expose private FontSize fontSize = DEFAULT;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        load();
    }

    @Override
    protected Enum<FontSize> getSelected() {
        return fontSize;
    }

    @Override
    public void setSelected(Enum<FontSize> selected) {
        this.fontSize = (FontSize) selected;
        save();
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Text Size";
    }

    public int getSize() {
        return fontSize.getSize();
    }


}
