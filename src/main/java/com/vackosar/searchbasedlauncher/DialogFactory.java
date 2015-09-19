package com.vackosar.searchbasedlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.widget.EditText;

import com.google.inject.Inject;

import java.util.Collection;

import roboguice.inject.ContextSingleton;

@SuppressWarnings("Convert2Lambda")
@ContextSingleton
public class DialogFactory {

    @Inject private Activity activity;
    @Inject private AppTypeSelector appTypeSelector;

    public static final String HIDE = "Hide";
    public static final String UNINSTALL = "Uninstall";
    public static final String RENAME = "Rename";
    public static final String REMOVE = "Remove";
    public static final String ADD = "Add";

    public void showNormalOptions(final App app, final AppsManager appsManager) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(app.getNick());
        if (!appsManager.getExtra().contains(app) && !appsManager.getHidden().contains(app)) {
            showHideApp(app, dialog, appsManager);
        } else if (appsManager.getExtra().contains(app) && appsManager.getHidden().contains(app)) {
            showRenamedApp(app, dialog, appsManager);
        } else if (appsManager.getExtra().contains(app)) {
            showExtraAddedApp(app, dialog, appsManager);
        } else {
            appsManager.getHidden().remove(app);
            appsManager.save();
        }
        dialog.create().show();
    }

    public void showUnhideAppOptions(final App app, final AppsManager appsManager) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(app.getNick());
        EditText dialogInput = new EditText(activity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("Remove this activity (appsManager.getHidden() app) from appsManager.getHidden() applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton(REMOVE, createRemoveListener(app, appsManager, appsManager.getHidden()));
        dialog.create().show();
    }

    private DialogInterface.OnClickListener createRemoveListener(final App app, final AppsManager appsManager, final Collection<App> collection) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                collection.remove(app);
                appsManager.save();
                appsManager.refreshView();
                dialog.dismiss();
            }
        };
    }

    public void showRemoveExtraAppOptions(final App app, final AppsManager appsManager) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(app.getNick());
        EditText dialogInput = new EditText(activity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("Remove this (appsManager.getExtra() added list parseViewId all activities) activity from applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton(REMOVE, createRemoveListener(app, appsManager, appsManager.getExtra()));
        dialog.create().show();
    }


    public void showAddExtraAppOptions(App app, final AppsManager appsManager) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(app.getNick());
        EditText dialogInput = new EditText(activity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("Add this activity to applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton(ADD, createAddListener(app, appsManager, dialogInput));
        dialog.create().show();
    }

    private DialogInterface.OnClickListener createAddListener(final App app, final AppsManager appsManager, final EditText dialogInput) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appsManager.getExtra().add(app);
                app.setNick(dialogInput.getText().toString());
                appsManager.save();
                dialog.dismiss();
            }
        };
    }

    public void showExtraAddedApp(final App app, AlertDialog.Builder dialog, final AppsManager appsManager) {
        dialog.setMessage("Remove activity " + app + " from appsManager.getExtra() added (to applications list) list ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton(REMOVE, createRemoveListener(app, appsManager, appsManager.getExtra()));
    }

    public void showRenamedApp(final App app, AlertDialog.Builder dialog, final AppsManager appsManager) {
        EditText dialogInput = new EditText(activity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("This application is in both add and hide lists, thus is probably renamed.");
        dialog.setCancelable(true);
        dialog.setPositiveButton(HIDE, createHideListener(app, appsManager));
        dialog.setNeutralButton(UNINSTALL, createUninstallListener(app, appsManager));
        dialog.setNegativeButton(RENAME, createRenameListener(app, appsManager, dialogInput));
    }

    private DialogInterface.OnClickListener createHideListener(final App app, final AppsManager appsManager) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appsManager.getExtra().remove(app);
                appsManager.getHidden().add(app);
                appsManager.save();
                dialog.dismiss();
            }
        };
    }

    public void showHideApp(final App app, AlertDialog.Builder dialog, final AppsManager appsManager) {
        EditText dialogInput = new EditText(activity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("Hide this application from applications list, rename application (add to Hide and Extra list with diferent names) or uninstall it?");
        dialog.setCancelable(true);
        dialog.setPositiveButton(HIDE, createHideListener(app, appsManager));
        dialog.setNeutralButton(UNINSTALL, createUninstallListener(app, appsManager));
        dialog.setNegativeButton(RENAME, createRenameListener(app, appsManager, dialogInput));
    }

    private DialogInterface.OnClickListener createRenameListener(final App app, final AppsManager appsManager, final EditText dialogInput) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appsManager.getHidden().add(app);
                app.setNick(dialogInput.getText().toString());
                appsManager.getExtra().remove(app);
                appsManager.getExtra().add(app);
                appsManager.save();
                dialog.dismiss();
            }
        };
    }

    private DialogInterface.OnClickListener createUninstallListener(final App app, final AppsManager appsManager) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                activity.startActivity(intent);
                appsManager.load();
                dialog.dismiss();
            }
        };
    }

    public void showOptionsForApp(App app, AppsManager appsManager) {
        switch (appTypeSelector.getSelected()) {
            case normal:
                showNormalOptions(app, appsManager);
                break;
            case activity:
                showAddExtraAppOptions(app, appsManager);
                break;
            case extra:
                showRemoveExtraAppOptions(app, appsManager);
                break;
            case hidden:
                showUnhideAppOptions(app, appsManager);
                break;
        }
    }
}
