package com.vackosar.searchbasedlauncher.entity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.boundary.SearchText;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AppExecutor {
    @Inject private SearchText searchText;
    @Inject private Activity activity;

    public void act(App app) {
        try {
            searchText.setActivatedColor();
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(app.getName(), app.getActivity()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            activity.startActivity(intent);
            searchText.clearText();
            searchText.setNormalColor();
        } catch (Exception e) {
            e.printStackTrace();
            searchText.setNormalColor();
        }
    }
}
