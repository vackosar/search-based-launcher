package com.vackosar.searchbasedlauncher.control;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.boundary.FontSizeSelector;

import java.util.List;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ListAdapterFactory {

    @Inject private FontSizeSelector fontSizeSelector;
    @Inject private Activity activity;

    public ListAdapter create(List<String> list) {
        return new ArrayAdapter<String>(activity, R.layout.list_item, R.id.listItemText, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                fontSizeSelector.setSize((TextView) row.findViewById(R.id.listItemText));
                return row;
            }
        };
    }
}
