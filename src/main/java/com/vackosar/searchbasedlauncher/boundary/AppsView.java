package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.AppsManager;
import com.vackosar.searchbasedlauncher.control.DialogFactory;
import com.vackosar.searchbasedlauncher.control.PackageAddedOrRemovedEvent;
import com.vackosar.searchbasedlauncher.control.TextViewManager;
import com.vackosar.searchbasedlauncher.entity.App;
import com.vackosar.searchbasedlauncher.entity.AppExecutor;
import com.vackosar.searchbasedlauncher.entity.AppsFactory;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnStartEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppsView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Indentifiable {

    @InjectView(R.id.appListView) private ListView listView;
    @Inject private AutostartSelector autostartSelector;
    @Inject private DialogFactory dialogFactory;
    @Inject private Activity activity;
    @Inject private SearchText searchText;
    @Inject private AppExecutor appExecutor;
    @Inject private AppsManager appsManager;
    @Inject private EventManager eventManager;
    @Inject private AppsFactory appsFactory;
    @Inject private TextViewManager textViewManager;
    @Inject private SingletonPersister<AppsView> persister;
    private boolean isAppFound = false;

    @Expose private List<App> recent = new ArrayList<>();

    public static final int MAX_RECENT_COUNT = 15;
    public static final int FIRST_INDEX = 0;

    private final List<App> filtered = new ArrayList<>();

    @SuppressWarnings("unused")
    public void onCreate(@Observes OnCreateEvent onCreate) {
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        load();
        searchText.setSearchTextCallback(new SearchText.TextChangedCallback() {
            @Override
            public void call() {
                if (!isAppFound){
                    refreshView();
                }
            }
        });
        appsManager.setRefreshCallback(new AppsManager.RefreshCallback() {
            @Override
            public void refresh() {
                refreshView();
            }
        });
    }

    @SuppressWarnings("unused")
    public void onStart(@Observes OnStartEvent onStartEvent) {
        if (isAppFound){
            isAppFound = false;
            filtered.clear();
            searchText.clearText();
        }
        appsManager.load();
    }

    private void load() {
        persister.load(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        executeActivity(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dialogFactory.showOptionsForApp(getApp(position), appsManager);
        return false;
    }

    public void refreshView() {
        String filterText = searchText.getFilterText();
        addFiltered(filterText);
        if (autostartFirstApp()) {
            executeActivity(FIRST_INDEX);
        } else {
            viewAppList(filtered);
        }
    }

    private void addFiltered(String filterText) {
        filtered.clear();

        addRecentToFiltered(filterText);
        for (App app: appsManager.getPkg()) {
            addFiltredIfMatch(filterText, app);
        }
    }

    private void addFiltredIfMatch(String filterText, App app) {
        if (checkMatch(filterText, app) && !recent.contains(app)) {
            filtered.add(app);
        }
    }

    private void addRecentToFiltered(String filterText) {
        for (App app: getReversedRecent()) {
            if (checkMatch(filterText, app) && appsManager.getPkg().contains(app)) {
                filtered.add(app);
            }
        }
    }

    private boolean checkMatch(String filterText, App app) {
        return app.getNick().toLowerCase().matches(filterText);
    }

    private boolean autostartFirstApp() {
        return filtered.size() == 1 && appsManager.getPkg().size() > 1 && autostartSelector.isOn();
    }

    private List<App> getReversedRecent() {
        final List<App> reverseRecent = new ArrayList<>(recent);
        Collections.reverse(reverseRecent);
        return reverseRecent;
    }

    private void viewAppList(List<App> appList) {
        final List<String> list = new ArrayList<>();
        for (App app: appList) {
            list.add(app.getNick());
        }
        listView.setAdapter(textViewManager.createListAdapter(list));
    }

    public void executeActivity(int index) {
        final App app = getApp(index);
        addNewRecent(app);
        appExecutor.act(app);
        isAppFound = true;
    }

    private App getApp(int index) {
        return filtered.get(index);
    }

    public void addNewRecent(App app) {
        recent.remove(app);
        recent.add(app);
        if (recent.size() > MAX_RECENT_COUNT) {
            recent.remove(FIRST_INDEX);
        }
        save();
    }

    private void save() {
        persister.save(this);
    }

    @SuppressWarnings("unused")
    public void onPackageAddedOrRemovedEvent(@Observes PackageAddedOrRemovedEvent packageAddedOrRemovedEvent) {
        recent.retainAll(appsFactory.getAllActivities());
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    public void clearRecent() {
        recent.clear();
        save();
        refreshView();
    }
}
