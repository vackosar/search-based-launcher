package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.entity.Action;

import java.util.ArrayList;
import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class MenuList implements AdapterView.OnItemClickListener {
    @InjectView(R.id.menuList) private ListView view;
    @Inject private Activity activity;
    @Inject private ItemListSelector itemListSelector;
    @Inject private AutostartSelector autostartSelector;
    @Inject private BackgroundSelector backgroundSelector;

    private List<Action> list;

    @SuppressWarnings("unused")
    private void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        loadActions();
        view.setOnItemClickListener(this);
    }

    private void loadActions() {
        list = new ArrayList<>();
        list.add(itemListSelector);
        list.add(autostartSelector);
        list.add(backgroundSelector);
        loadNames();
    }

    private void loadNames() {
        final List<String> names = extractNames();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.list_item, android.R.id.text1, names);
        view.setAdapter(adapter);
    }

    private List<String> extractNames() {
        List<String> names = new ArrayList<>();
        for (Action action: list) {
            names.add(action.getName());
        }
        return names;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startSelected(position);
    }

    private void startSelected(int position) {
        list.get(position).act();
    }
}
