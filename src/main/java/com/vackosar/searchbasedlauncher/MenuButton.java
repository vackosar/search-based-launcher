package com.vackosar.searchbasedlauncher;

import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.inject.Inject;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class MenuButton extends Colorful implements View.OnClickListener {
    public static final int MENU_CHILD_ID = 1;
    @InjectView(R.id.viewAnimator) ViewAnimator viewAnimator;
    @InjectView(R.id.menuButton) TextView textView;
    @Inject SearchText searchText;
    @Inject AppsManager appsManager;

    public void onCreateEvent(@Observes OnCreateEvent OnCreateEvent) {
        textView.setOnClickListener(this);
    }

    public void toggle(@Observes ToggleEvent toggleEvent) {
        searchText.setNormalColor();
        toggleView();
        if (! isMenuShown()) {
            appsManager.load();
        }
    }

    private boolean isMenuShown() {
        return viewAnimator.getDisplayedChild() == MENU_CHILD_ID;
    }

    private void toggleView() {
        viewAnimator.showNext();
    }

    @Override
    public View getView() {
        return textView;
    }

    @Override
    public void onClick(View v) {
        toggle(null);
    }

    public static class ToggleEvent {
    }

}
