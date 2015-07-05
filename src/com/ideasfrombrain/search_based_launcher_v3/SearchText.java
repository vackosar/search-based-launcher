package com.ideasfrombrain.search_based_launcher_v3;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class SearchText implements TextWatcher, Colorful {
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    final MainActivity mainActivity;
    final EditText editText;

    public SearchText (MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        editText = (EditText) mainActivity.findViewById(R.id.editText1);
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
        mainActivity.refresh();
    }

    @Override
    public View getView() {
        return editText;
    }

    public void clearText() {
        editText.setText(EMPTY);
    }

    public void setSpaceCharacterToText() {
        editText.setText(SPACE);
    }

    public String getFilterText () {
        // space is replaced with REGEX  symbols ".*"
        return editText.getText().toString().toLowerCase().replace(" ", ".*") + ".*";
    }
}
