package com.vackosar.searchbasedlauncher.boundary;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.Colorful;
import com.vackosar.searchbasedlauncher.control.RegexEscaper;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class SearchText extends Colorful implements TextWatcher {

    @InjectView(R.id.searchText) EditText editText;
    @Inject RegexEscaper regexEscaper;

    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String REGEX_MATCH_ALL = ".*";
    private TextChangedCallback textChangedCallback;

    public void onCreate(@Observes OnCreateEvent onCreate) {
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

    @Override
    public View getView() {
        return editText;
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

    public interface TextChangedCallback {
        void call();
    }
}
