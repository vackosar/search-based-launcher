package com.ideasfrombrain.searchbasedlauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    final MainActivity mainActivity;
    final ListView listView;
    private final DialogFactory dialogFactory;
    List<App> filtered = new ArrayList<>();
    List<App> recent = new ArrayList<>();

    public static final int FIRST_INDEX = 0;

    public AppsView(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        listView = (ListView) mainActivity.findViewById(R.id.appListView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        dialogFactory = new DialogFactory(mainActivity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        runApp(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return showOptionsForApp(position);
    }

    public void refeshView() {
        String filterText = mainActivity.getSearchText().getFilterText();
        addRecent(filterText);
        addFiltered(filterText);
        if (autostartFirstApp()) {
            runApp(FIRST_INDEX);
        } else {
            viewAppList(filtered);
        }
    }

    private void addFiltered(String filterText) {
        filtered.clear();
        for (App app: mainActivity.getAppsManager().getPkg()) {
            if (app.getNick().toLowerCase().matches(filterText) && !recent.contains(app)) {
                filtered.add(app);
            }
        }
    }

    private void addRecent(String filterText) {
        recent.retainAll(mainActivity.getAppsManager().getPkg());
        for (App app: getReversedRecent()) {
            if (app.getNick().toLowerCase().matches(filterText)) {
                filtered.add(app.getAsRecent());
            }
        }
    }

    private boolean autostartFirstApp() {
        return filtered.size() == 1 && mainActivity.getAppsManager().getPkg().size() > 1 && mainActivity.getAutostartButton().isOn();
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

    public void runApp(int index) {
        final List<App> pkg = mainActivity.getAppsManager().getPkg();
        final App app = pkg.get(pkg.indexOf(filtered.get(index)));
        mainActivity.getSearchText().setActivatedColor();
        addRecent(app);
        if (app.isMenu()) {
            mainActivity.getMenu().toggle();
        } else {
            try {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName(app.getName(), app.getActivity()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                mainActivity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                mainActivity.getSearchText().setNormalColor();
            }
        }
    }

    public void addRecent(App app) {
        recent.remove(app);
        recent.add(app);
        if (recent.size() > 10) {
            recent.remove(FIRST_INDEX);
        }
    }

    public boolean showOptionsForApp(final int appIndex) {
        final App app = filtered.get(appIndex);
        if (app.equals(AppsManager.MENU_APP)) {
            return false;
        }

        switch (mainActivity.getMenu().getAppTypeSelector().getSelected()) {
            case normal:
                dialogFactory.showNormalOptions(app);
                break;
            case activity:
                dialogFactory.showAddExtraAppOptions(app);
                break;
            case extra:
                dialogFactory.showRemoveExtraAppOptions(app);
                break;
            case hidden:
                dialogFactory.showUnhideAppOptions(app);
                break;
        }
        return false;
    }
}
