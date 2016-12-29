package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vackosar.searchbasedlauncher.entity.Action;
import com.vackosar.searchbasedlauncher.entity.AppsView;

@Singleton
public class ClearHistoryAndRestart extends Action {

    @Inject private AppsView appsView;
    @Inject private Activity activity;

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Clear & Restart";
    }

    @Override
    public void act() {
        appsView.clearRecent();
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
}
