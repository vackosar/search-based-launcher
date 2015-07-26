package com.ideasfrombrain.search_based_launcher_v3;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppsManager {
    final MainActivity mainActivity;
    private final AppListView appListView;
    List<App> pkg = new ArrayList<>();
    List<App> filtered = new ArrayList<>();
    Set<App> extra = new HashSet<>();
    Set<App> hidden = new HashSet<>();
    final List<App> recent = new ArrayList<>();
    final private PreferencesAdapter preferencesAdapter;
    public static final App MENU_APP = new App(MainActivity.APP_PACKAGE_NAME + ".Menu", " Menu-Launcher", MainActivity.APP_PACKAGE_NAME + ".Menu");

    public AppsManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        preferencesAdapter = new PreferencesAdapter(mainActivity);
        appListView = new AppListView(mainActivity);
    }
    
    public void reload() {
        refreshView();
        recent.retainAll(pkg);
        extra.retainAll(pkg);
        hidden.retainAll(pkg);
        filtered.retainAll(pkg);
        saveExtRemLists();
        refreshView();
        generateFiltered();
    }

    public void generateFiltered() {
        filtered.clear();
        String filterText = mainActivity.getSearchText().getFilterText();
        for (App app: recent) {
            if (app.getNick().toLowerCase().matches(filterText)) {
                filtered.add(app.getAsRecent());
            }
        }
        for (App app: pkg) {
            if (app.getNick().toLowerCase().matches(filterText) && (recent.contains(app))) {
                filtered.add(app);
            }
        }
        if (filtered.size() == 1 && mainActivity.getAutostartButton().isOn()) {
            runApp(0);
        } else {
            appListView.viewAppList(filtered);
        }
    }

    public void refreshView() {
        Log.d("DEBUG", "start loading apps");
        Log.d("DEBUG", "activity arrays prepared");

        final Intent main = new Intent(Intent.ACTION_MAIN, null);
        final PackageManager pm = mainActivity.getPackageManager();

        pkg.clear();

        switch (mainActivity.getRadioButtons().getCheckedRadioButton()) {
            case 0:
                loadNormal(main, pm);
                break;
            case 1:
                loadAll(main, pm);
                break;
            case 2:
                pkg.addAll(extra);
                break;
            case 3:
                pkg.addAll(hidden);
                break;
        }
        pkg.add(MENU_APP);
        generateFiltered();
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

        main.addCategory(Intent.CATEGORY_LAUNCHER); // will show only Regular AppsManager
        final List<ResolveInfo> launchables = pm.queryIntentActivities(main, 0);

        for (ResolveInfo launchable : launchables) {
            String nick = launchable.activityInfo.name;
            String name = launchable.activityInfo.packageName;
            String activity = launchable.activityInfo.loadLabel(pm).toString();
            final App app = new App(name, nick, activity);
            if (!hidden.contains(app)) {
                pkg.add(app);
            }
        }
    }

    public void loadExtRemLists() {
        try {
            extra = preferencesAdapter.loadSet("extra");
            hidden = preferencesAdapter.loadSet("hidden");
        } catch (Exception e) {
            saveExtRemLists();
        }
    }

    public void saveExtRemLists() {
        preferencesAdapter.saveSet(extra, "extra");
        preferencesAdapter.saveSet(hidden, "hidden");
    }

    private void setAppLists(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadExtRemLists();
            refreshView();
        } else {
            pkg = new ArrayList<>(App.getApps(new HashSet<>(savedInstanceState.getStringArrayList("pkg"))));
            extra = App.getApps(new HashSet<>(savedInstanceState.getStringArrayList("extra")));
            hidden = App.getApps(new HashSet<>(savedInstanceState.getStringArrayList("hidden")));
        }
    }

    public void runApp(int appIndex) {
        final App app = filtered.get(appIndex);
        mainActivity.getSearchText().setActivatedColor();
        recent.remove(app);
        if (app.isMenu()) {
            mainActivity.showNext(false);
        } else {
            try {
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName(app.getName(), app.getActivity()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                mainActivity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                mainActivity.getSearchText().setNormalColor();
            }
        }
    }
    
    public void saveState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList("pkg", new ArrayList<>(App.getJson(new HashSet<>(pkg))));
        savedInstanceState.putStringArrayList("extra", new ArrayList<>(App.getJson(extra)));
        savedInstanceState.putStringArrayList("hidden", new ArrayList<>(App.getJson(hidden)));
    }

    public boolean showOptionsForApp(final int appIndex) {
        final App app = filtered.get(appIndex);
        if (app.equals(MENU_APP.getActivity())) {
            return false;
        }

        switch (mainActivity.getRadioButtons().getCheckedRadioButton()) {
            case 0:
                showNormalOptions(app);
                break;
            case 1:
                showAddExtraAppOptions(app);
                break;
            case 2:
                showRemoveExtraAppOptions(app);
                break;
            case 3:
                showUnhideAppOptions(app);
                break;
        }
        return false;
    }

    private void showUnhideAppOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());

        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Remove this activity (hidden app) from hidden applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (hidden.remove(app)) {
                    saveExtRemLists();
                    refreshView();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }

    private void showRemoveExtraAppOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());

        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Remove this (extra added list of all activities) activity from applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (extra.remove(app) | recent.remove(app)) {
                    saveExtRemLists();
                    refreshView();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }

    private void showAddExtraAppOptions(App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());

        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Add this activity to applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                extra.add(app);
                app.setNick(dialogInput.getText().toString());
                saveExtRemLists();
                refreshView();
                dialog.dismiss();
            }
        });

        dialog.create().show();
    }

    private void showNormalOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());
        if (!extra.contains(app) && !hidden.contains(app)) {
            showHideApp(app, dialog);
        } else if (extra.contains(app) && hidden.contains(app)) {
            showRenamedApp(app, dialog);
        } else if (extra.contains(app)) {
            showExtraAddedApp(app, dialog);
        } else {
            //FIXME
        }
        dialog.create().show();
    }

    private void showExtraAddedApp(final App app, AlertDialog.Builder dialog) {
        dialog.setMessage("Remove activity " + app + " from extra added (to applications list) list ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                extra.remove(app);
                saveExtRemLists();
                refreshView();
                dialog.dismiss();
            }
        });
    }

    private void showRenamedApp(final App app, AlertDialog.Builder dialog) {
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("This application is in both add and hide lists, thus is probably renamed.");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (hidden.contains(app)) {
                    extra.remove(app);
                    saveExtRemLists();
                    refreshView();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                mainActivity.startActivity(intent);
                reload();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                app.setNick(dialogInput.getText().toString());
                saveExtRemLists();
                refreshView();
                dialog.dismiss();
            }
        });
    }

    private void showHideApp(final App app, AlertDialog.Builder dialog) {
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Hide this application from applications list, rename application (add to Hide and Extra list with diferent names) or uninstall it?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (hidden.remove(app)) {
                    saveExtRemLists();
                    refreshView();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                mainActivity.startActivity(intent);
                refreshView();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!hidden.contains(app)) {
                    hidden.add(app);
                }
                app.setNick(dialogInput.getText().toString());
                extra.add(app);
                recent.remove(app);
                recent.add(app);
                saveExtRemLists();
                refreshView();
                dialog.dismiss();
            }
        });
    }
}
