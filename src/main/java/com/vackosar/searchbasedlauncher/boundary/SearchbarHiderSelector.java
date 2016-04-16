package com.vackosar.searchbasedlauncher.boundary;

import android.view.View;
import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.entity.SelectAction;
import com.vackosar.searchbasedlauncher.entity.YesNo;

import roboguice.inject.InjectView;

public class SearchbarHiderSelector extends SelectAction<YesNo> {

    public static final YesNo DEFAULT = YesNo.No;

    @InjectView(R.id.searchText) private EditText view;
    @Expose private YesNo hidden = DEFAULT;

    private void sync() {
        if (hidden.bool) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void load() {
        super.load();
        sync();
    }

    @Override
    protected Enum<YesNo> getSelected() {
        return hidden;
    }

    @Override
    public void setSelected(Enum<YesNo> selected) {
        this.hidden = (YesNo) selected;
        sync();
        save();
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Hiding Searchbar";
    }
}
