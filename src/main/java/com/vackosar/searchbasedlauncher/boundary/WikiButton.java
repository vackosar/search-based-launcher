package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.R;
import com.vackosar.searchbasedlauncher.control.Colorful;

import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

public class WikiButton extends Colorful implements View.OnClickListener{

    @InjectView(R.id.wikiButton) private TextView textView;
    @Inject private Activity activity;

    public static final String WIKI_URI = "https://github.com/vackosar/search-based-launcher/wiki";

    public void onCreateEvent(@Observes OnCreateEvent onCreateEvent) {
        textView.setOnClickListener(this);
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WIKI_URI));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(marketIntent);
    }
}
