package com.vackosar.searchbasedlauncher.control;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.inject.Inject;

import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnDestroyEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class PackageAddedOrRemovedEvent {

    @Inject
    private EventManager eventManager;

    @Inject
    private Activity activity;

    private BroadcastReceiver broadcastReceiver;

    private IntentFilter createFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        return filter;
    }

    private BroadcastReceiver getBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                eventManager.fire(new PackageAddedOrRemovedEvent());
            }
        };
    }

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        broadcastReceiver = getBroadcastReceiver();
        activity.registerReceiver(broadcastReceiver, createFilter());
    }

    public void onDestroyEvent(@Observes OnDestroyEvent onDestroyEvent) {
        activity.unregisterReceiver(broadcastReceiver);
    }
}
