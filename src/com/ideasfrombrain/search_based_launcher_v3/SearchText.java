package com.ideasfrombrain.search_based_launcher_v3;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SearchText implements TextWatcher {
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
}
