package com.ideasfrombrain.search_based_launcher_v3;


import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


import android.view.inputmethod.InputMethodManager;

import android.widget.ViewAnimator;

import android.widget.EditText;
import android.widget.RadioGroup;

import org.json.JSONException;


@SuppressWarnings("Convert2Lambda")
public class MainActivity extends Activity {


    public static final int FIRST_INDEX = 0;
    static String APP_PACKAGE_NAME = "com.ideasfrombrain.search_based_launcher_v3";
    public static final App MENU_APP = new App(APP_PACKAGE_NAME + ".Menu", " Menu-Launcher", APP_PACKAGE_NAME + ".Menu");

    boolean newerAndroidVersion = true;

    List<App> pkg = new ArrayList<App>();
    List<App> pkgFiltered = new ArrayList<App>();
    List<App> extra = new ArrayList<App>();
    List<App> hidden = new ArrayList<App>();
    List<App> recent = new ArrayList<App>();

    EditText DialogInput;

    private final BroadcastReceiver mPkgApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApps();
            recent.retainAll(pkg);
            extra.retainAll(pkg);
            hidden.retainAll(pkg);
            pkgFiltered.retainAll(pkg);
            saveExtRemLists(false);
            loadApps();
            refresh();
        }
    };
    private SearchText searchText;
    private AppListView appListView;
    private AutostartButton autostartButton;
    private WifiButton wifiButton;
    private BluetoothButton bluetoothButton;
    private FlashButton flashButton;
    private CameraButton cameraButton;
    private RadioButtons radioButtons;

    private void registerIntentReceivers() {
        IntentFilter pkgFilter = new IntentFilter();
        pkgFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        pkgFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pkgFilter.addDataScheme("package");
        registerReceiver(mPkgApplicationsReceiver, pkgFilter);
    }

    public void loadApps() {
        Log.d("DEBUG", "start loading apps");
        Log.d("DEBUG", "activity arrays prepared");

        final Intent main = new Intent(Intent.ACTION_MAIN, null);
        final PackageManager pm = getPackageManager();

        pkg.clear();

        switch (radioButtons.getCheckedRadioButton()) {
            case 0:
                loadNormal(main, pm);
                break;
            case 1:
                loadAll(main, pm);
                break;
            case 2:
                loadExtra();
                break;
            case 3:
                loadHidden();
                break;
        }
        pkg.add(MENU_APP);

    }

    private void loadHidden() {
        pkg.addAll(hidden);
    }

    private void loadExtra() {
        pkg.addAll(extra);
    }

    private void loadAll(Intent main, PackageManager pm) {
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);
        for (ResolveInfo launchable : launchables) {
            App app = new App(launchable.activityInfo.packageName, deriveNick(launchable), launchable.activityInfo.name);
            pkg.add(app);
        }
    }

    private String deriveNick(ResolveInfo launchable) {
        String[] split = launchable.activityInfo.name.split("\\.");
        String nick = split[1];
        for (int j = 2; j < split.length; j++) {
            nick = nick + ":" + split[j];
        }
        return nick;
    }

    private void loadNormal(Intent main, PackageManager pm) {
        pkg.addAll(extra);

        main.addCategory(Intent.CATEGORY_LAUNCHER); // will show only Regular Apps
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);

        for (ResolveInfo launchable : launchables) {
            String nick = launchable.activityInfo.name;
            if (ItemNumInHide(nick) == -1) {
                String name = launchable.activityInfo.packageName;
                String activity = launchable.activityInfo.loadLabel(pm).toString();
                pkg.add(new App(name, nick, activity));
            }
        }
    }

    public void loadExtRemLists(boolean check) {
        try {
            final Context context = getApplicationContext();
            extra = loadList("extra", context);
            hidden = loadList("hidden", context);
            recent = loadList("recent", context);
        } catch (Exception e) {
            //Log.d("DEBUG","excep. during load" + e.getStackTrace().toString());
            saveExtRemLists(false);
        }
    }

    public void saveExtRemLists(boolean check) {
        final Context myContext = getApplicationContext();
        saveList(extra, "extra", myContext);
        saveList(hidden, "hidden", myContext);
        saveList(recent, "recent", myContext);
    }

    public boolean saveList(List<App> list, String listName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(listName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> set = new HashSet<>(App.getJsonList(list));
        editor.putStringSet(listName, set);
        return editor.commit();
    }

    public List<App> loadList(String listName, Context mContext) throws JSONException {
        List<App> list = new ArrayList<>();
        SharedPreferences prefs = mContext.getSharedPreferences(listName, 0);
        final Set<String> jsonSet = prefs.getStringSet(listName, null);
        return App.getAppList(new ArrayList<>(jsonSet));
    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_MENU) {
            myShowNext(false);
        } else if (keycode == KeyEvent.KEYCODE_SEARCH) {
            startSearch("", false, null, true);
        } else if (keycode == KeyEvent.KEYCODE_BACK) {
            ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
            if (mViewAnimator.getDisplayedChild() == 1) {
                myShowNext(false);
            }
        } else {
            return super.onKeyUp(keycode, event);
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appListView = new AppListView(this);
        searchText = new SearchText(this);
        autostartButton = new AutostartButton(this);
        wifiButton = new WifiButton(this);
        bluetoothButton = new BluetoothButton(this);
        flashButton = new FlashButton(this);
        cameraButton = new CameraButton(this);
        createMenuDonateButton();
        radioButtons = new RadioButtons(this);
        setAndroidVersion();
        setAppLists(savedInstanceState);
        registerIntentReceivers();
    }

    private void createMenuDonateButton() {
        findViewById(R.id.donateButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME));
                marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(marketIntent);
            }
        });
    }

    private void setAppLists(Bundle savedInstanceState) {
        if (savedInstanceState == null) {


            loadExtRemLists(false);
            loadApps();
        } else {

            pkg.Name = savedInstanceState.getStringArrayList("pkg.Name");
            pkg.Nick = savedInstanceState.getStringArrayList("pkg.Nick");
            pkg.Activity = savedInstanceState.getStringArrayList("pkg.Activity");

            extra.Name = savedInstanceState.getStringArrayList("extra.Name");
            extra.Nick = savedInstanceState.getStringArrayList("extra.Nick");
            extra.Activity = savedInstanceState.getStringArrayList("extra.Activity");

            hidden.Name = savedInstanceState.getStringArrayList("hidden.Name");
            hidden.Nick = savedInstanceState.getStringArrayList("hidden.Nick");
            hidden.Activity = savedInstanceState.getStringArrayList("hidden.Activity");

            recent.Name = savedInstanceState.getStringArrayList("recent.Name");
            recent.Nick = savedInstanceState.getStringArrayList("recent.Nick");
            recent.Activity = savedInstanceState.getStringArrayList("recent.Activity");

        }
    }

    private void setAndroidVersion() {
        String Aversion = android.os.Build.VERSION.RELEASE;
        if (Aversion.startsWith("1.") ||
                Aversion.startsWith("2.0") ||
                Aversion.startsWith("2.1")) {
            newerAndroidVersion = false;
        } else {
            newerAndroidVersion = true;
        }
    }


    public void myShowNext(Boolean DoLoadApps) {
        searchText.setNormalColor();
        ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
        //mViewAnimator.setInAnimation(null);  // Skipping this will cause trouble
        // mViewAnimator.setOutAnimation(null);
        mViewAnimator.showNext();

        if (mViewAnimator.getDisplayedChild() == 0) {
            //final TextView myToggleButton = (TextView) findViewById(R.id.toggleButton0);
            if (DoLoadApps) {
                loadApps();
            }

            if (radioButtons.getCheckedRadioButton() > 0) {
                searchText.setSpaceCharacterToText();
                radioButtons.setInvisible();
            } else {
                wifiButton.setVisibleIfAvailable();
                bluetoothButton.setVisibleIfAvailable();
                cameraButton.setVisibleIfAvailable();
                searchText.clearText();
            }

        } else {
            RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
            mRadioGroup.requestFocus();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        radioButtons.save();
        savedInstanceState.putBoolean("newerAndroidVersion", newerAndroidVersion);

        savedInstanceState.putStringArrayList("pkg.Name", pkg.Name);
        savedInstanceState.putStringArrayList("pkg.Nick", pkg.Nick);
        savedInstanceState.putStringArrayList("pkg.Activity", pkg.Activity);

        savedInstanceState.putStringArrayList("extra.Name", extra.Name);
        savedInstanceState.putStringArrayList("extra.Nick", extra.Nick);
        savedInstanceState.putStringArrayList("extra.Activity", extra.Activity);

        savedInstanceState.putStringArrayList("hidden.Name", hidden.Name);
        savedInstanceState.putStringArrayList("hidden.Nick", hidden.Nick);
        savedInstanceState.putStringArrayList("hidden.Activity", hidden.Activity);

        savedInstanceState.putStringArrayList("recent.Name", recent.Name);
        savedInstanceState.putStringArrayList("recent.Nick", recent.Nick);
        savedInstanceState.putStringArrayList("recent.Activity", recent.Activity);

        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchText.setNormalColor();
        if ((radioButtons.getCheckedRadioButton() == 0) && autostartButton.isAutostart()) {
            searchText.clearText();
        }

        if (!newerAndroidVersion) {
            final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }


    }


    @Override
    public void onDestroy() {
        wifiButton.unregisterReceiver();
        unregisterReceiver(mPkgApplicationsReceiver);
        super.onDestroy();
    }

    public void runApp(int index) {
        searchText.setActivatedColor();

        int tmpint = ItemNumInRecent(pkgFiltered.Activity.get(index));
        if ((recent.Nick.size() >= 10) && (tmpint == -1)) {
            recent.Name.remove(0);
            recent.Nick.remove(0);
            recent.Activity.remove(0);
        } else if (tmpint != -1) {
            recent.Name.remove(tmpint);
            recent.Nick.remove(tmpint);
            recent.Activity.remove(tmpint);
        }
        final Context myContext = getApplicationContext();
        saveList(recent.Name, "recent.Name", myContext);
        saveList(recent.Nick, "recent.Nick", myContext);
        saveList(recent.Activity, "recent.Activity", myContext);

        String tmpNickBefore = pkgFiltered.Nick.get(index);
        recent.Name.add(pkgFiltered.Name.get(index));
        if ((tmpNickBefore.matches("R:.*"))) {
            tmpNickBefore = tmpNickBefore.substring(2, tmpNickBefore.length());
        }
        recent.Nick.add(tmpNickBefore);
        recent.Activity.add(pkgFiltered.Activity.get(index));

        if (!newerAndroidVersion) {
            final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }


        if ((APP_PACKAGE_NAME + ".Menu").equals(pkgFiltered.Name.get(index))) {
            myShowNext(false);
        } else {
            try {

                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                intent.setComponent(new ComponentName(pkgFiltered.Name.get(index), pkgFiltered.Activity.get(index)));


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.d("DEBUG", e.getMessage());
                searchText.setNormalColor();

                if (!newerAndroidVersion) {
                    final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        }

    }


    public void refresh() {
        pkgFiltered.Name.clear();
        pkgFiltered.Nick.clear();
        pkgFiltered.Activity.clear();
        String FilterText = searchText.getFilterText();


        int tmpsize = recent.Name.size();
        for (int i = 0; i < tmpsize; i++) {
            //Log.d("DEBUG", recent.Name.get(tmpsize-1-i));
            if (recent.Nick.get(tmpsize - 1 - i).toLowerCase().matches(FilterText)) {
                pkgFiltered.Name.add(recent.Name.get(tmpsize - 1 - i));
                pkgFiltered.Activity.add(recent.Activity.get(tmpsize - 1 - i));
                pkgFiltered.Nick.add("R:" + recent.Nick.get(tmpsize - 1 - i));
            }
        }

        for (int i = 0; i < pkg.Name.size(); i++) {

            if ((pkg.Nick.get(i).toLowerCase().matches(FilterText)) && (ItemNumInRecent(pkg.Activity.get(i)) == -1)) {
                pkgFiltered.Name.add(pkg.Name.get(i));
                pkgFiltered.Activity.add(pkg.Activity.get(i));
                pkgFiltered.Nick.add(pkg.Nick.get(i));
                //Log.d("DEBUG", pkg.Nick[i]);
            }

        }
        if (((pkgFiltered.Name.size()) == 1) && autostartButton.isAutostart()) {
            runApp(FIRST_INDEX);
        } else {
            appListView.setAppList(pkgFiltered.Nick);
        }

        //}


    }

    //@Override
    public boolean showOptionsForApp(final int index) {
        final String activity = pkgFiltered.Activity.get(index);
        final String name = pkgFiltered.Name.get(index);
        final String nickBefore = pkgFiltered.Nick.get(index);

        if ((activity == APP_PACKAGE_NAME + ".Menu")) {
            return false;
        }

        String nick;
        if ((nickBefore.matches("R:.*"))) {
            nick = nickBefore.substring(2, nickBefore.length());
        } else {
            nick = nickBefore;
        }

        switch (radioButtons.getCheckedRadioButton()) {
            case 0:
                showNormalOptions(activity, name, nick);
                break;
            case 1:
                showAddExtraAppOptions(index, nick);
                break;
            case 2:
                showRemoveExtraAppOptions(index, activity, nick);
                break;
            case 3:
                showUnhideAppOptions(index, activity, nick);
                break;
        }
        return false;
    }

    private void showUnhideAppOptions(int index, final String tmpActivity, String nick) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(nick);

        DialogInput = new EditText(this);
        DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        DialogInput.setText(pkgFiltered.Nick.get(index));

        dialog.setView(DialogInput);
        dialog.setMessage("Remove this activity (hidden app) from hidden applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int tmpItemNumInHide = MainActivity.this.ItemNumInHide(tmpActivity);
                if (tmpItemNumInHide != -1) {
                    hidden.Activity.remove(tmpItemNumInHide);
                    hidden.Name.remove(tmpItemNumInHide);
                    hidden.Nick.remove(tmpItemNumInHide);
                    MainActivity.this.saveExtRemLists(false);
                    loadApps();
                    refresh();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }

    private void showRemoveExtraAppOptions(int index, final String tmpActivity, final String nick) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(nick);

        DialogInput = new EditText(this);
        DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        DialogInput.setText(pkgFiltered.Nick.get(index));

        dialog.setView(DialogInput);
        dialog.setMessage("Remove this (extra added list of all activities) activity from applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int tmpItemNumInExtra = MainActivity.this.ItemNumInExtra(tmpActivity);
                if (tmpItemNumInExtra != -1) {
                    extra.Activity.remove(tmpItemNumInExtra);
                    extra.Name.remove(tmpItemNumInExtra);
                    extra.Nick.remove(tmpItemNumInExtra);

                    int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
                    if (tmpItemNumInRecent != -1) {
                        recent.Nick.remove(tmpItemNumInRecent);
                        recent.Name.remove(tmpItemNumInRecent);
                        recent.Activity.remove(tmpItemNumInRecent);
                    }

                    MainActivity.this.saveExtRemLists(false);
                    loadApps();
                    refresh();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }

    private void showAddExtraAppOptions(final int index, String nick) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(nick);

        DialogInput = new EditText(this);
        DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        DialogInput.setText(pkgFiltered.Nick.get(index));

        dialog.setView(DialogInput);
        dialog.setMessage("Add this activity to applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                extra.Name.add(pkgFiltered.Name.get(index));
                extra.Nick.add(DialogInput.getText().toString());
                extra.Activity.add(pkgFiltered.Activity.get(index));
                saveExtRemLists(true);
                loadApps();
                refresh();
                dialog.dismiss();
            }
        });

        dialog.create().show();
    }

    private void showNormalOptions(final String tmpActivity, final String tmpName, final String nick) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(nick);

        int tmpItemNumInExtra = MainActivity.this.ItemNumInExtra(tmpActivity);

        if ((tmpItemNumInExtra == -1) && (MainActivity.this.ItemNumInHide(tmpActivity) == -1)) {
            showHideApp(tmpActivity, tmpName, nick, dialog);
        } else if ((tmpItemNumInExtra != -1) && (MainActivity.this.ItemNumInHide(tmpActivity) != -1)) {
            showRenamedApp(tmpActivity, tmpName, nick, dialog);
        } else if (tmpItemNumInExtra != -1) {
            showExtraAddedApp(tmpActivity, dialog);
        }

        dialog.create().show();
    }

    private void showExtraAddedApp(final String tmpActivity, AlertDialog.Builder dialog) {
        dialog.setMessage("Remove activity " + tmpActivity + " from extra added (to applications list) list ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int tmpItemNumInExtra = MainActivity.this.ItemNumInExtra(tmpActivity);
                extra.Activity.remove(tmpItemNumInExtra);
                extra.Name.remove(tmpItemNumInExtra);
                extra.Nick.remove(tmpItemNumInExtra);

                int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
                if (tmpItemNumInRecent != -1) {
                    recent.Nick.remove(tmpItemNumInRecent);
                    recent.Name.remove(tmpItemNumInRecent);
                    recent.Activity.remove(tmpItemNumInRecent);
                }

                saveExtRemLists(false);
                loadApps();
                refresh();
                dialog.dismiss();

            }
        });
    }

    private void showRenamedApp(final String tmpActivity, final String tmpName, String nick, AlertDialog.Builder dialog) {
        DialogInput = new EditText(this);
        DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        DialogInput.setText(nick);

        dialog.setView(DialogInput);
        dialog.setMessage("This application is in both add and hide lists, thus is probably renamed.");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int tmpItemNumInExtra = MainActivity.this.ItemNumInExtra(tmpActivity);
                if (MainActivity.this.ItemNumInHide(tmpActivity) == -1) {
                    //hidden.Name.add(tmpName);
                    //hidden.Nick.add(tmpNick);
                    //hidden.Activity.add(tmpActivity);
                    extra.Nick.remove(tmpItemNumInExtra);
                    extra.Name.remove(tmpItemNumInExtra);
                    extra.Activity.remove(tmpItemNumInExtra);

                    int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
                    if (tmpItemNumInRecent != -1) {
                        recent.Nick.remove(tmpItemNumInRecent);
                        recent.Name.remove(tmpItemNumInRecent);
                        recent.Activity.remove(tmpItemNumInRecent);
                    }

                    saveExtRemLists(false);
                    loadApps();
                    refresh();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + tmpName));
                startActivity(intent);
                //loadApps();
                refresh();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int tmpItemNumInExtra = MainActivity.this.ItemNumInExtra(tmpActivity);
                //hidden.Name.add(tmpName);
                //hidden.Nick.add(tmpNick);
                //hidden.Activity.add(tmpActivity);

                extra.Nick.remove(tmpItemNumInExtra);
                extra.Name.remove(tmpItemNumInExtra);
                extra.Activity.remove(tmpItemNumInExtra);

                extra.Nick.add(DialogInput.getText().toString());
                extra.Name.add(tmpName);
                extra.Activity.add(tmpActivity);

                int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
                if (tmpItemNumInRecent != -1) {
                    recent.Nick.set(tmpItemNumInRecent, DialogInput.getText().toString());
                }

                saveExtRemLists(false);
                loadApps();
                refresh();
                dialog.dismiss();

            }
        });
    }

    private void showHideApp(final String tmpActivity, final String tmpName, final String nick, AlertDialog.Builder dialog) {
        DialogInput = new EditText(this);
        DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        DialogInput.setText(nick);

        dialog.setView(DialogInput);
        dialog.setMessage("Hide this application from applications list, rename application (add to Hide and Extra list with diferent names) or uninstall it?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);
                if (MainActivity.this.ItemNumInHide(tmpActivity) == -1) {
                    hidden.Name.add(tmpName);
                    hidden.Nick.add(nick);
                    hidden.Activity.add(tmpActivity);

                    int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
                    if (tmpItemNumInRecent != -1) {
                        recent.Nick.remove(tmpItemNumInRecent);
                        recent.Name.remove(tmpItemNumInRecent);
                        recent.Activity.remove(tmpItemNumInRecent);
                    }

                    saveExtRemLists(false);
                    loadApps();
                    refresh();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + tmpName));
                startActivity(intent);
                //loadApps();
                refresh();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (MainActivity.this.ItemNumInHide(tmpActivity) == -1) {
                    hidden.Name.add(tmpName);
                    hidden.Nick.add(nick);
                    hidden.Activity.add(tmpActivity);

                    extra.Nick.add(DialogInput.getText().toString());
                    extra.Name.add(tmpName);
                    extra.Activity.add(tmpActivity);

                    int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
                    if (tmpItemNumInRecent != -1) {
                        recent.Nick.set(tmpItemNumInRecent, DialogInput.getText().toString());
                    }

                    saveExtRemLists(false);
                    loadApps();
                    refresh();
                    dialog.dismiss();
                }
            }
        });
    }

    public int ItemNumInExtra(String Activity) { // NOTE: if not found returns -1
        for (int i = 0; i < extra.Activity.size(); i++) {
            if (Activity.equals(extra.Activity.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int ItemNumInHide(String Activity) { // NOTE: if not found returns -1
        for (int i = 0; i < hidden.Activity.size(); i++) {
            if (Activity.equals(hidden.Activity.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int ItemNumInRecent(String Activity) { // NOTE: if not found returns -1
        for (int i = 0; i < recent.Activity.size(); i++) {
            if (Activity.equals(recent.Activity.get(i))) {
                return i;
            }
        }
        return -1;
    }

}