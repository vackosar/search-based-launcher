package com.vackosar.searchbasedlauncher.boundary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.Action;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class WikiAction extends Action{

    @Inject private Activity activity;

    public static final String WIKI_URI = "https://github.com/vackosar/search-based-launcher/wiki";

    @Override
    public String getId() {
        return getClass().getName();
    }

    @Override
    public String getName() {
        return "Request feature";
    }

    @Override
    public void act() {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WIKI_URI));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        activity.startActivity(marketIntent);
    }
}
