package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.TextViewManager;
import com.vackosar.searchbasedlauncher.entity.Action;

import java.util.ArrayList;
import java.util.Arrays;
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
    @Inject private TextViewManager textViewManager;
    @Inject private SizeSelector sizeSelector;
    @Inject private AlignmentSelector alignmentSelector;
    @Inject private KeyboardHiderSelector keyboardHiderSelector;
    @Inject private WikiAction wikiAction;
    @Inject private ClearHistory clearHistory;

    private List<Action> list;

    @SuppressWarnings("unused")
    private void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        loadActions();
        view.setOnItemClickListener(this);
    }

    private void loadActions() {
        list = new ArrayList<>();
        list.addAll(Arrays.asList(
                  itemListSelector
                , autostartSelector
                , themeSelector
                , searchbarHiderSelector
                , sizeSelector
                , alignmentSelector
                , keyboardHiderSelector
                , wikiAction
                , clearHistory));
        loadNames();
    }

    private void loadNames() {
        final List<String> names = extractNames();
        view.setAdapter(textViewManager.createListAdapter(names));
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
