package com.vackosar.searchbasedlauncher;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

public class DonateButton implements Colorful, View.OnClickListener{
    final MainActivity mainActivity;
    private final TextView textView;

    public DonateButton(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        textView = (TextView) mainActivity.findViewById(R.id.donateButton);
        textView.setOnClickListener(this);
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=vackosar%40gmail%2ecom&lc=US&item_name=ideasfrombrain&item_number=Search%20based%20launcher&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest"));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        mainActivity.startActivity(marketIntent);
    }
}
