package com.vackosar.searchbasedlauncher.control;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.boundary.FontSizeSelector;

import java.util.List;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class TextViewManager {

    @Inject private FontSizeSelector fontSizeSelector;
    @InjectView(R.id.viewAnimator) private ViewGroup viewGroup;
    @Inject private Activity activity;

    @SuppressWarnings("unused")
    private void onCreate(@Observes OnCreateEvent onCreate) {
        configureChildren(viewGroup);
    }

    public void configure(TextView child) {
        child.setTextSize(TypedValue.COMPLEX_UNIT_PT, fontSizeSelector.getSize());
    }

    private void configureChildren(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                configureChildren((ViewGroup) child);
            } else {
                if (child != null) {
                    if (child instanceof TextView) {
                        configure((TextView) child);
                    }
                }
            }
        }
    }

    public ListAdapter createListAdapter(List<String> list) {
        return new ArrayAdapter<String>(activity, R.layout.list_item, R.id.listItemText, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                configure((TextView) row.findViewById(R.id.listItemText));
                return row;
            }
        };
    }
}
