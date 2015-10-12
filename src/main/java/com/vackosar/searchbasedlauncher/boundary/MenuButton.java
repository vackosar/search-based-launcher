package com.vackosar.searchbasedlauncher.boundary;

import android.view.View;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.AppsManager;
import com.vackosar.searchbasedlauncher.control.Colorful;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

@ContextSingleton
public class MenuButton extends Colorful implements View.OnClickListener {
    @InjectView(R.id.viewAnimator) ViewAnimator viewAnimator;
    @InjectView(R.id.menu) TextView textView;
    @Inject private SearchText searchText;
    @Inject private AppsManager appsManager;

    public static final int MENU_CHILD_ID = 1;

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
    public TextView getView() {
        return textView;
    }

    @Override
    public void onClick(View v) {
        toggle(null);
    }

    public static class ToggleEvent {
    }

}
