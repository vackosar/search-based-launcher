package com.vackosar.searchbasedlauncher.boundary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vackosar.searchbasedlauncher.entity.Action;

@Singleton
public class ClearHistory extends Action {

    @Inject private AppsView appsView;

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Clear History";
    }

    @Override
    public void act() {
        appsView.clearRecent();
    }
}
