package com.vackosar.searchbasedlauncher.boundary;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.annotations.Expose;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.RegexEscaper;
import com.vackosar.searchbasedlauncher.control.YesNo;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class SearchText implements TextWatcher, AdapterView.OnItemSelectedListener, Indentifiable {

    @InjectView(R.id.searchText) private EditText editText;
    @InjectView(R.id.searchTextHider) private Spinner hider;
    @Inject private RegexEscaper regexEscaper;
    @Inject private SingletonPersister<SearchText> persister;

    @Expose private boolean hidden = false;

    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String REGEX_MATCH_ALL = ".*";
    private TextChangedCallback textChangedCallback;

    private void onCreate(@Observes OnCreateEvent onCreate) {
        editText.addTextChangedListener(this);
        hider.setOnItemSelectedListener(this);
        load();
    }

    private void load() {
        persister.load(this);
        hider.setSelection(YesNo.valueOf(hidden).position);
        sync();
    }

    private void sync() {
        if (hidden) {
            editText.setVisibility(View.GONE);
        } else {
            editText.setVisibility(View.VISIBLE);
        }
    }

    private void save() {
        persister.save(this);
        sync();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        textChangedCallback.call();
    }

    public void setSearchTextCallback(TextChangedCallback textChangedCallback) {
        this.textChangedCallback = textChangedCallback;
    }

    public void clearText() {
        editText.setText(EMPTY);
    }

    public String getFilterText () {
        return regexEscaper.act(editText.getText().toString())
                .toLowerCase()
                .replace(SPACE, REGEX_MATCH_ALL)
                + REGEX_MATCH_ALL;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hidden = YesNo.valueOf(position).bool;
        save();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    public interface TextChangedCallback {
        void call();
    }
}
