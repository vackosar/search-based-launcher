package com.vackosar.searchbasedlauncher.boundary;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.AppsView;
import com.vackosar.searchbasedlauncher.entity.RecentsCount;
import com.vackosar.searchbasedlauncher.entity.SelectAction;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class RecentsCountSelector extends SelectAction<RecentsCount> {

    private static final RecentsCount DEFAULT = RecentsCount._15;

    @Inject private AppsView appsView;
    @Expose private RecentsCount recentsCount = DEFAULT;

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        load();
        sync();
    }

    private void sync() {
        appsView.setRecentsCount(((RecentsCount) getSelected()).getCount());
    }

    protected @Override Enum<RecentsCount> getSelected() {
        return recentsCount;
    }

    @Override
    public void setSelected(Enum<RecentsCount> selected) {
        this.recentsCount = (RecentsCount) selected;
        save();
        sync();
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Recents Count";
    }

    public int getSize() {
        return recentsCount.getCount();
    }


}
