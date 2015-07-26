package com.ideasfrombrain.search_based_launcher_v3;

import android.content.SharedPreferences;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RadioButtons implements RadioGroup.OnCheckedChangeListener {
    final private MainActivity mainActivity;
    private int checkedRadioButton;
    private final RadioGroup radioGroup;
    private final SharedPreferences sharedPreferences;
    private final ColorService colorService = new ColorService();
    private final TextView button1;
    private final TextView button2;
    private final TextView button3;
    private final TextView button4;

    public RadioButtons(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        radioGroup = (RadioGroup) mainActivity.findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(this);
        sharedPreferences = mainActivity.getApplicationContext().getSharedPreferences("preferencename", 0);
        checkedRadioButton = sharedPreferences.getInt("checkedRadioButton", 0);
        button1 = (TextView) mainActivity.findViewById(R.id.button1);
        button2 = (TextView) mainActivity.findViewById(R.id.button2);
        button3 = (TextView) mainActivity.findViewById(R.id.button4);
        button4 = (TextView) mainActivity.findViewById(R.id.button6);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio0:
                checkedRadioButton = 0;
                break;
            case R.id.radio1:
                checkedRadioButton = 1;
                break;
            case R.id.radio2:
                checkedRadioButton = 2;
                break;
            case R.id.radio3:
                checkedRadioButton = 3;
                break;
        }
        mainActivity.showNext(true);
    }

    public void setInvisible() {
        colorService.setInvisible(button1);
        colorService.setInvisible(button2);
        colorService.setInvisible(button3);
        colorService.setInvisible(button4);
    }

    public int getCheckedRadioButton() {
        return checkedRadioButton;
    }

    public void save() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("checkedRadioButton", checkedRadioButton);
        editor.commit();
    }
}
