package com.vackosar.searchbasedlauncher.boundary;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.RegexEscaper;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class SearchText implements TextWatcher, Indentifiable {

    @InjectView(R.id.searchText) private EditText editText;
    @Inject private RegexEscaper regexEscaper;
    @Inject private SingletonPersister<SearchText> persister;

    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String REGEX_MATCH_ALL = ".*";
    private TextChangedCallback textChangedCallback;

    private void onCreate(@Observes OnCreateEvent onCreate) {
        editText.addTextChangedListener(this);
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
    public String getId() {
        return getClass().getName();
    }

    public interface TextChangedCallback {
        void call();
    }
}
