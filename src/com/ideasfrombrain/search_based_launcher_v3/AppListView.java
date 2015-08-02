package com.ideasfrombrain.search_based_launcher_v3;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AppListView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    final MainActivity mainActivity;
    final ListView listView;
    private final DialogFactory dialogFactory;
    List<App> filtered = new ArrayList<>();
    List<App> recent = new ArrayList<>();

    public static final int FIRST_INDEX = 0;

    public AppListView (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        listView = (ListView) mainActivity.findViewById(R.id.listView1);
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
        filtered.clear();
        recent.retainAll(mainActivity.getAppListManager().getPkg());
        String filterText = mainActivity.getSearchText().getFilterText();
        for (App app: recent) {
            if (app.getNick().toLowerCase().matches(filterText)) {
                filtered.add(app.getAsRecent());
            }
        }
        for (App app: mainActivity.getAppListManager().getPkg()) {
            if (app.getNick().toLowerCase().matches(filterText) && !recent.contains(app)) {
                filtered.add(app);
            }
        }
        if (filtered.size() == 1 && mainActivity.getAutostartButton().isOn()) {
            runApp(FIRST_INDEX);
        } else {
            viewAppList(filtered);
        }
    }

    private void viewAppList(List<App> appList) {
        final List<String> list = new ArrayList<String>();
        for (App app: appList) {
            list.add(app.getNick());
        }
        listView.setAdapter(new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, android.R.id.text1, list));
    }

    public void runApp(int index) {
        final List<App> pkg = mainActivity.getAppListManager().getPkg();
        final App app = pkg.get(pkg.indexOf(filtered.get(index)));
        mainActivity.getSearchText().setActivatedColor();
        addRecent(app);
        if (app.isMenu()) {
            mainActivity.getMenu().toggle(false);
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
        if (app.equals(AppListManager.MENU_APP)) {
            return false;
        }

        switch (mainActivity.getMenu().getAppListSelector().getSelected()) {
            case 0:
                dialogFactory.showNormalOptions(app);
                break;
            case 1:
                dialogFactory.showAddExtraAppOptions(app);
                break;
            case 2:
                dialogFactory.showRemoveExtraAppOptions(app);
                break;
            case 3:
                dialogFactory.showUnhideAppOptions(app);
                break;
        }
        return false;
    }
}
