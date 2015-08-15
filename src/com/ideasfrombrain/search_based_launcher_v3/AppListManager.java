package com.ideasfrombrain.search_based_launcher_v3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("Convert2Lambda")
public class AppListManager {
    final MainActivity mainActivity;
    List<App> pkg = getEmptyAppList();
    Set<App> extra = new HashSet<>();
    Set<App> hidden = new HashSet<>();
    final private PreferencesAdapter preferencesAdapter;
    public static final App MENU_APP = new App(MainActivity.APP_PACKAGE_NAME + ".Menu", " Menu-Launcher", MainActivity.APP_PACKAGE_NAME + ".Menu");

    public AppListManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        preferencesAdapter = new PreferencesAdapter(mainActivity);
    }

    public void refreshView() {
        mainActivity.getAppListView().refeshView();
    }

    public void reload() {
        final Intent main = new Intent(Intent.ACTION_MAIN, null);
        final PackageManager pm = mainActivity.getPackageManager();
        switch (mainActivity.getMenu().getAppTypeSelector().getSelected()) {
            case normal:
                pkg = getApplicationActivities(main, pm);
                pkg.removeAll(hidden);
                pkg.addAll(extra);
                break;
            case activity:
                pkg = getAllActivities(main, pm);
                pkg.removeAll(hidden);
                pkg.addAll(extra);
                break;
            case extra:
                pkg = getEmptyAppList();
                pkg.addAll(extra);
                break;
            case hidden:
                pkg = getEmptyAppList();
                pkg.addAll(hidden);
                break;
        }
        pkg.add(MENU_APP);
        Collections.sort(pkg);
        mainActivity.getAppListView().refeshView();
    }

    private List<App> getAllActivities(Intent main, PackageManager pm) {
        final List<App> pkg = getEmptyAppList();
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);
        for (ResolveInfo launchable : launchables) {
            App app = new App(launchable.activityInfo.packageName, deriveNick(launchable), launchable.activityInfo.name);
            pkg.add(app);
        }
        return pkg;
    }

    private static List<App> getEmptyAppList() {
        return new ArrayList<>();
    }

    private String deriveNick(ResolveInfo launchable) {
        String[] split = launchable.activityInfo.name.split("\\.");
        String nick = split[1];
        for (int j = 2; j < split.length; j++) {
            nick = nick + ":" + split[j];
        }
        return nick;
    }

    private List<App> getApplicationActivities(Intent main, PackageManager pm) {
        final List<App> pkg = getEmptyAppList();
        main.addCategory(Intent.CATEGORY_LAUNCHER); // will show only Regular AppListManager
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);

        for (ResolveInfo launchable : launchables) {
            String nick = launchable.activityInfo.loadLabel(pm).toString();
            String name = launchable.activityInfo.packageName;
            String activity = launchable.activityInfo.name;
            final App app = new App(name, nick, activity);
            pkg.add(app);
        }
        return pkg;
    }

    public void load() {
        try {
            extra = preferencesAdapter.loadSet("extra");
            hidden = preferencesAdapter.loadSet("hidden");
        } catch (Exception e) {
            save();
        }
        reload();
    }

    public void save() {
        preferencesAdapter.saveSet(extra, "extra");
        preferencesAdapter.saveSet(hidden, "hidden");
        refreshView();
    }

    public void loadFromSavedState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            load();
        } else {
            pkg = new ArrayList<>(App.getApps(new HashSet<>(savedInstanceState.getStringArrayList("pkg"))));
            extra = App.getApps(new HashSet<>(savedInstanceState.getStringArrayList("extra")));
            hidden = App.getApps(new HashSet<>(savedInstanceState.getStringArrayList("hidden")));
        }
    }
    
    public void saveState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("pkg", new ArrayList<>(App.getJson(new HashSet<>(pkg))));
        savedInstanceState.putStringArrayList("extra", new ArrayList<>(App.getJson(extra)));
        savedInstanceState.putStringArrayList("hidden", new ArrayList<>(App.getJson(hidden)));
    }

    public Set<App> getExtra() {
        return extra;
    }

    public Set<App> getHidden() {
        return hidden;
    }

    public List<App> getPkg() {
        return pkg;
    }
}
