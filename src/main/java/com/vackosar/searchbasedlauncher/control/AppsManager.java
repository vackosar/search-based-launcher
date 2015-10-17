package com.vackosar.searchbasedlauncher.control;

import android.app.Activity;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.boundary.ItemListSelector;
import com.vackosar.searchbasedlauncher.entity.App;
import com.vackosar.searchbasedlauncher.entity.AppsFactory;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@SuppressWarnings("Convert2Lambda")
@ContextSingleton
public class AppsManager {

    @Inject private ItemListSelector itemListSelector;
    @Inject private PackageAddedOrRemovedEvent packageAddedOrRemovedEvent;
    @Inject private Activity activity;
    @Inject private AppsFactory appsFactory;
    @Inject private SingletonPersister<AppsManager> persister;

    @Expose private final Set<App> extra = new HashSet<>();
    @Expose private final Set<App> hidden = new HashSet<>();

    private final List<App> pkg = AppsFactory.getEmptyAppList();
    private RefreshCallback refreshCallback;

    public void refreshView() {
        refreshCallback.refresh();
    }

    public void setRefreshCallback(RefreshCallback refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    private void reload() {
        pkg.clear();
        switch (itemListSelector.getSelected()) {
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
        persister.load(this);
        reload();
    }

    public void save() {
        persister.save(this);
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
