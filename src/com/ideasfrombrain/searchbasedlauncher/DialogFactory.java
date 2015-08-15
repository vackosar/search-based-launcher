package com.ideasfrombrain.searchbasedlauncher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.widget.EditText;

@SuppressWarnings("Convert2Lambda")
public class DialogFactory {
    private final MainActivity mainActivity;

    public DialogFactory(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void showNormalOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());
        AppsManager appsManager = mainActivity.getAppsManager();
        if (!appsManager.getExtra().contains(app) && !appsManager.getHidden().contains(app)) {
            showHideApp(app, dialog);
        } else if (appsManager.getExtra().contains(app) && appsManager.getHidden().contains(app)) {
            showRenamedApp(app, dialog);
        } else if (appsManager.getExtra().contains(app)) {
            showExtraAddedApp(app, dialog);
        } else {
            //FIXME
        }
        dialog.create().show();
    }

    public void showUnhideAppOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());
        AppsManager appsManager = mainActivity.getAppsManager();
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Remove this activity (appsManager.getHidden() app) from appsManager.getHidden() applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appsManager.getHidden().remove(app)) {
                    appsManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }

    public void showRemoveExtraAppOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());
        AppsManager appsManager = mainActivity.getAppsManager();
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Remove this (appsManager.getExtra() added list parseViewId all activities) activity from applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appsManager.getExtra().remove(app)) {
                    appsManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }


    public void showAddExtraAppOptions(App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());
        AppsManager appsManager = mainActivity.getAppsManager();
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Add this activity to applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appsManager.getExtra().add(app);
                app.setNick(dialogInput.getText().toString());
                appsManager.save();
                dialog.dismiss();
            }
        });

        dialog.create().show();
    }

    public void showExtraAddedApp(final App app, AlertDialog.Builder dialog) {
        AppsManager appsManager = mainActivity.getAppsManager();
        dialog.setMessage("Remove activity " + app + " from appsManager.getExtra() added (to applications list) list ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appsManager.getExtra().remove(app);
                appsManager.save();
                dialog.dismiss();
            }
        });
    }

    public void showRenamedApp(final App app, AlertDialog.Builder dialog) {
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        AppsManager appsManager = mainActivity.getAppsManager();
        dialog.setView(dialogInput);
        dialog.setMessage("This application is in both add and hide lists, thus is probably renamed.");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appsManager.getHidden().contains(app)) {
                    appsManager.getExtra().remove(app);
                    appsManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                mainActivity.startActivity(intent);
                appsManager.reload();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                app.setNick(dialogInput.getText().toString());
                appsManager.save();
                dialog.dismiss();
            }
        });
    }

    public void showHideApp(final App app, AlertDialog.Builder dialog) {
        AppsManager appsManager = mainActivity.getAppsManager();
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("Hide this application from applications list, rename application (add to Hide and Extra list with diferent names) or uninstall it?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appsManager.getHidden().remove(app)) {
                    appsManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                mainActivity.startActivity(intent);
                appsManager.reload();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!appsManager.getHidden().contains(app)) {
                    appsManager.getHidden().add(app);
                }
                app.setNick(dialogInput.getText().toString());
                appsManager.getExtra().add(app);
                appsManager.save();
                dialog.dismiss();
            }
        });
    }
}
