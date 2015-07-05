package com.ideasfrombrain.search_based_launcher_v3;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AppListView implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    final MainActivity mainActivity;
    final ListView listView;

    public AppListView (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        listView = (ListView) mainActivity.findViewById(R.id.listView1);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mainActivity.runApp(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return mainActivity.showOptionsForApp(position);
    }
}
