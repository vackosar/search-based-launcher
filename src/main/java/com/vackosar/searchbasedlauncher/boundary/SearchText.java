package com.vackosar.searchbasedlauncher.boundary;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.RegexEscaper;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;
import com.vackosar.searchbasedlauncher.entity.SingletonPersister;
import com.vackosar.searchbasedlauncher.entity.YesNo;

import java.util.Arrays;

import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class SearchText implements TextWatcher, Indentifiable, View.OnFocusChangeListener {

    @InjectView(R.id.searchText) private EditText editText;
    @Inject private RegexEscaper regexEscaper;
    @Inject private SingletonPersister<SearchText> persister;
    @Inject private ThemeSelector themeSelector;
    @Inject private KeyboardHiderSelector keyboardHiderSelector;

    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String REGEX_MATCH_ALL = ".*";
    private TextChangedCallback textChangedCallback;

    private void onCreate(@Observes OnCreateEvent onCreate) {
        // Fix for missing android:editTextColor in API 10-.
        if (Arrays.asList(ThemeSelector.Theme.Black, ThemeSelector.Theme.Wallpaper).contains(themeSelector.getSelected())) {
            editText.setTextColor(Color.WHITE);
        }
    }

    private void onResume(@Observes OnResumeEvent onResumeEvent) {
        editText.addTextChangedListener(this);
        clearText();
        editText.setOnFocusChangeListener(this);
        editText.requestFocus();

    }

    private void onPause(@Observes OnPauseEvent onPauseEvent){
        editText.setOnFocusChangeListener(null);
        editText.removeTextChangedListener(this);
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

    public void requestFocus(){
        editText.requestFocus();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (editText.equals(v)){
            if (hasFocus &&
                    keyboardHiderSelector.getSelected().equals(YesNo.No) &&
                    editText.getVisibility() == View.VISIBLE
                    ){
                InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            } else{
                InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    public interface TextChangedCallback {
        void call();
    }
}
