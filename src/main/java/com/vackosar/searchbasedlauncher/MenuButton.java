package com.vackosar.searchbasedlauncher;

import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class MenuButton extends Colorful implements View.OnClickListener {
    public static final int MENU_CHILD_ID = 1;
    private final MainActivity mainActivity;
    private final TextView textView;
    private final SearchText searchText;
    private final AppsManager appsManager;
    private final AppTypeSelector appTypeSelector;

    public MenuButton(MainActivity mainActivity, SearchText searchText, AppsManager appsManager, AppTypeSelector appTypeSelector) {
        this.mainActivity = mainActivity;
        this.searchText = searchText;
        this.appsManager = appsManager;
        this.appTypeSelector = appTypeSelector;
        textView = (TextView) mainActivity.findViewById(R.id.menuButton);
        textView.setOnClickListener(this);
    }

    public void toggle() {
        searchText.setNormalColor();
        toggleView();
        if (! isMenuShown()) {
            appsManager.reload();
        } else {
            appTypeSelector.requestFocus();
        }
    }

    private boolean isMenuShown() {
        return getViewAnimator().getDisplayedChild() == MENU_CHILD_ID;
    }

    private void toggleView() {
        ViewAnimator mViewAnimator = getViewAnimator();
        mViewAnimator.showNext();
    }

    private ViewAnimator getViewAnimator() {
        return (ViewAnimator) mainActivity.findViewById(R.id.viewAnimator);
    }

    @Override
    public View getView() {
        return textView;
    }

    @Override
    public void onClick(View v) {
        toggle();
    }
}
