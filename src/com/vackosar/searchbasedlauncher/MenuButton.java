package com.vackosar.searchbasedlauncher;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class MenuButton implements View.OnClickListener, Colorful {
    public static final int MENU_CHILD_ID = 1;
    private final MainActivity mainActivity;
    private final TextView textView;

    public MenuButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.menuButton);
        textView.setOnClickListener(this);
        createMenuDonateButton();
    }

    public void toggle() {
        mainActivity.getSearchText().setNormalColor();
        toggleView();
        if (! isMenuShown()) {
            mainActivity.getAppsManager().reload();
        } else {
            mainActivity.getAppTypeSelector().requestFocus();
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

    private void createMenuDonateButton() {
        mainActivity.findViewById(R.id.donateButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mainActivity.APP_PACKAGE_NAME));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                mainActivity.startActivity(marketIntent);
            }
        });
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
