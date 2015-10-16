package com.vackosar.searchbasedlauncher.entity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.boundary.BluetoothToggle;
import com.vackosar.searchbasedlauncher.boundary.WifiToggle;

import java.util.ArrayList;
import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class AppsFactory {

    @Inject private Activity activity;
    @Inject private WifiToggle wifiToggle;
    @Inject private BluetoothToggle bluetoothToggle;

    private PackageManager packageManager;

    public List<App> getAllActivities() {
        final List<App> pkg = getEmptyAppList();
        final List<ResolveInfo> launchables = packageManager.queryIntentActivities(createMainIntent(), 0);
        for (ResolveInfo launchable : launchables) {
            App app = new App(launchable.activityInfo.packageName, deriveNick(launchable), launchable.activityInfo.name);
            pkg.add(app);
        }
        return pkg;
    }

    public static List<App> getEmptyAppList() {
        return new ArrayList<>();
    }

    public List<App> getApplicationActivities() {
        final List<App> pkg = getEmptyAppList();
        final Intent mainIntent = createMainIntent();
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER); // will show only Regular AppsManager
        final List<ResolveInfo> launchables = packageManager.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo launchable : launchables) {
            String nick = launchable.activityInfo.loadLabel(packageManager).toString();
            String name = launchable.activityInfo.packageName;
            String activity = launchable.activityInfo.name;
            final App app = new App(name, nick, activity);
            pkg.add(app);
        }
        pkg.add(wifiToggle.getApp());
        pkg.add(bluetoothToggle.getApp());
        return pkg;
    }

    private Intent createMainIntent() {
        return new Intent(Intent.ACTION_MAIN, null);
    }

    private String deriveNick(ResolveInfo launchable) {
        String[] split = launchable.activityInfo.name.split("\\.");
        String nick = split[1];
        for (int j = 2; j < split.length; j++) {
            nick = nick + ":" + split[j];
        }
        return nick;
    }

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        packageManager = activity.getPackageManager();
    }
}
