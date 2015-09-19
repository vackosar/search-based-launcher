package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.AppsManager;
import com.vackosar.searchbasedlauncher.control.PackageAddedOrRemovedEvent;
import com.vackosar.searchbasedlauncher.control.PreferencesAdapter;
import com.vackosar.searchbasedlauncher.entity.App;
import com.vackosar.searchbasedlauncher.entity.AppsFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppsView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    @InjectView(R.id.appListView) private ListView listView;
    @Inject private AutostartButton autostartButton;
    @Inject private DialogFactory dialogFactory;
    @Inject private PreferencesAdapter preferencesAdapter;
    @Inject private Activity activity;
    @Inject private SearchText searchText;
    @Inject private AppsManager appsManager;
    @Inject private EventManager eventManager;
    @Inject private AppsFactory appsFactory;

    public static final String RECENT = "recent";
    private final List<App> filtered = new ArrayList<>();
    private List<App> recent;
    public static final int FIRST_INDEX = 0;

    public void onCreate(@Observes OnCreateEvent onCreate) {
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        recent = new ArrayList<>(preferencesAdapter.loadSet(RECENT));
        searchText.setSearchTextCallback(new SearchText.TextChangedCallback() {
            @Override
            public void call() {
                refreshView();
            }
        });
        appsManager.setRefreshCallback(new AppsManager.RefreshCallback() {
            @Override
            public void refresh() {
                refreshView();
            }
        });
        appsManager.load();
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
                filtered.add(app.getAsRecent());
            }
        }
    }

    private boolean checkMatch(String filterText, App app) {
        return app.getNick().toLowerCase().matches(filterText);
    }

    private boolean autostartFirstApp() {
        return filtered.size() == 1 && appsManager.getPkg().size() > 1 && autostartButton.isOn();
    }

    private List<App> getReversedRecent() {
        final ArrayList<App> reverseRecent = new ArrayList<App>(recent);
        Collections.reverse(reverseRecent);
        return reverseRecent;
    }

    private void viewAppList(List<App> appList) {
        final List<String> list = new ArrayList<String>();
        for (App app: appList) {
            list.add(app.getNick());
        }
        listView.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, list));
    }

    public void executeActivity(int index) {
        final App app = getApp(index);
        searchText.setActivatedColor();
        addNewRecent(app);
        if (app.isMenu()) {
            eventManager.fire(new MenuButton.ToggleEvent());
        } else {
            executeActivity(app);
        }
    }

    private void executeActivity(App app) {
        try {
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

    private App getApp(int index) {
        final List<App> pkg = appsManager.getPkg();
        return pkg.get(pkg.indexOf(filtered.get(index)));
    }

    public void addNewRecent(App app) {
        recent.remove(app);
        recent.add(app);
        if (recent.size() > 10) {
            recent.remove(FIRST_INDEX);
        }
        preferencesAdapter.saveSet(recent, RECENT);
    }

    public void onPackageAddedOrRemovedEvent(@Observes PackageAddedOrRemovedEvent packageAddedOrRemovedEvent) {
        recent.retainAll(appsFactory.getAllActivities());
    }
}
