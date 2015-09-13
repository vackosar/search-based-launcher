package com.vackosar.searchbasedlauncher;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

public class WikiButton extends Colorful implements View.OnClickListener{
    public static final String WIKI_URI = "https://github.com/vackosar/search-based-launcher/wiki";
    final MainActivity mainActivity;
    private final TextView textView;

    public WikiButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.wikiButton);
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
        mainActivity.startActivity(marketIntent);
    }
}
