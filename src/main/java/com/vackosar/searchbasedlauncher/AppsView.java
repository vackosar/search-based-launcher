package com.vackosar.searchbasedlauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class AppsView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    public static final String RECENT = "recent";
    @InjectView(R.id.appListView) ListView listView;
    private final List<App> filtered = new ArrayList<>();
    private final List<App> recent;
    public static final int FIRST_INDEX = 0;
    @Inject AutostartButton autostartButton;
    @Inject DialogFactory dialogFactory;
    @Inject PreferencesAdapter preferencesAdapter;
    @Inject MainActivity mainActivity;
    @Inject SearchText searchText;
    @Inject AppsManager appsManager;
//    @Inject
    MenuButton menuButton;

    public AppsView() {
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
        addRecent(filterText);
        for (App app: appsManager.getPkg()) {
            addFiltredIfMatch(filterText, app);
        }
    }

    private void addFiltredIfMatch(String filterText, App app) {
        if (checkMatch(filterText, app) && !recent.contains(app)) {
            filtered.add(app);
        }
    }

    private void addRecent(String filterText) {
        recent.retainAll(appsManager.getPkg());
        for (App app: getReversedRecent()) {
            if (checkMatch(filterText, app)) {
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
        listView.setAdapter(new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, android.R.id.text1, list));
    }

    public void executeActivity(int index) {
        final App app = getApp(index);
        searchText.setActivatedColor();
        addRecent(app);
        if (app.isMenu()) {
            menuButton.toggle();
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
            mainActivity.startActivity(intent);
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

    public void addRecent(App app) {
        recent.remove(app);
        recent.add(app);
        if (recent.size() > 10) {
            recent.remove(FIRST_INDEX);
        }
        preferencesAdapter.saveSet(recent, RECENT);
    }
}
