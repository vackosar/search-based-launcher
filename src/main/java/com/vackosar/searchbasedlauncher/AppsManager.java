package com.vackosar.searchbasedlauncher;

import android.app.Activity;

import com.google.inject.Inject;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@SuppressWarnings("Convert2Lambda")
@ContextSingleton
public class AppsManager {
    private final List<App> pkg = AppsFactory.getEmptyAppList();
    private final Set<App> extra = new HashSet<>();
    private final Set<App> hidden = new HashSet<>();
    private RefreshCallback refreshCallback;

    @Inject private AppTypeSelector appTypeSelector;
    @Inject private PreferencesAdapter preferencesAdapter;
    @Inject private PackageAddedOrRemovedEvent packageAddedOrRemovedEvent;
    @Inject private Activity activity;
    @Inject private AppsFactory appsFactory;

    public void refreshView() {
        refreshCallback.refresh();
    }

    public void setRefreshCallback(RefreshCallback refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    private void reload() {
        pkg.clear();
        switch (appTypeSelector.getSelected()) {
            case normal:
                pkg.addAll(appsFactory.getApplicationActivities());
                pkg.removeAll(hidden);
                pkg.addAll(extra);
                break;
            case activity:
                pkg.addAll(appsFactory.getAllActivities());
                pkg.removeAll(hidden);
                pkg.addAll(extra);
                break;
            case extra:
                pkg.addAll(AppsFactory.getEmptyAppList());
                pkg.addAll(extra);
                break;
            case hidden:
                pkg.addAll(AppsFactory.getEmptyAppList());
                pkg.addAll(hidden);
                break;
        }
        Collections.sort(pkg);
        refreshView();
    }

    public void load() {
        try {
            extra.clear();
            extra.addAll(preferencesAdapter.loadSet("extra"));
            hidden.clear();
            hidden.addAll(preferencesAdapter.loadSet("hidden"));
        } catch (Exception e) {
            save();
        }
        reload();
    }

    public void save() {
        preferencesAdapter.saveSet(extra, "extra");
        preferencesAdapter.saveSet(hidden, "hidden");
        reload();
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

    public interface RefreshCallback {
        void refresh();
    }

    public void onPackageAddedOrRemovedEvent(@Observes PackageAddedOrRemovedEvent packageAddedOrRemovedEvent) {
        reload();
    }
}
