package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.ListAdapterFactory;
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
    @Inject private ThemeSelector themeSelector;
    @Inject private SearchbarHiderSelector searchbarHiderSelector;
    @Inject private ListAdapterFactory listAdapterFactory;
    @Inject private FontSizeSelector fontSizeSelector;
    @Inject private WikiAction wikiAction;

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
        list.add(themeSelector);
        list.add(searchbarHiderSelector);
        list.add(fontSizeSelector);
        list.add(wikiAction);
        loadNames();
    }

    private void loadNames() {
        final List<String> names = extractNames();
        view.setAdapter(listAdapterFactory.create(names));
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
