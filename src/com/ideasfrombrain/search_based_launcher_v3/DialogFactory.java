package com.ideasfrombrain.search_based_launcher_v3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.widget.EditText;

@SuppressWarnings("Convert2Lambda")
public class DialogFactory {
    private final MainActivity mainActivity;
    private final AppListManager appListManager;

    public DialogFactory(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        appListManager = mainActivity.getAppListManager();
    }

    public void showNormalOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());
        if (!appListManager.getExtra().contains(app) && !appListManager.getHidden().contains(app)) {
            showHideApp(app, dialog);
        } else if (appListManager.getExtra().contains(app) && appListManager.getHidden().contains(app)) {
            showRenamedApp(app, dialog);
        } else if (appListManager.getExtra().contains(app)) {
            showExtraAddedApp(app, dialog);
        } else {
            //FIXME
        }
        dialog.create().show();
    }

    public void showUnhideAppOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());

        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Remove this activity (appListManager.getHidden() app) from appListManager.getHidden() applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appListManager.getHidden().remove(app)) {
                    appListManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }

    public void showRemoveExtraAppOptions(final App app) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle(app.getNick());

        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("Remove this (appListManager.getExtra() added list of all activities) activity from applications list?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appListManager.getExtra().remove(app)) {
                    appListManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.create().show();
    }


    public void showAddExtraAppOptions(App app) {
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
                appListManager.getExtra().add(app);
                app.setNick(dialogInput.getText().toString());
                appListManager.save();
                dialog.dismiss();
            }
        });

        dialog.create().show();
    }

    public void showExtraAddedApp(final App app, AlertDialog.Builder dialog) {
        dialog.setMessage("Remove activity " + app + " from appListManager.getExtra() added (to applications list) list ?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                appListManager.getExtra().remove(app);
                appListManager.save();
                dialog.dismiss();
            }
        });
    }

    public void showRenamedApp(final App app, AlertDialog.Builder dialog) {
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());

        dialog.setView(dialogInput);
        dialog.setMessage("This application is in both add and hide lists, thus is probably renamed.");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appListManager.getHidden().contains(app)) {
                    appListManager.getExtra().remove(app);
                    appListManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                mainActivity.startActivity(intent);
                appListManager.reload();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                app.setNick(dialogInput.getText().toString());
                appListManager.save();
                dialog.dismiss();
            }
        });
    }

    public void showHideApp(final App app, AlertDialog.Builder dialog) {
        EditText dialogInput = new EditText(mainActivity);
        dialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        dialogInput.setText(app.getNick());
        dialog.setView(dialogInput);
        dialog.setMessage("Hide this application from applications list, rename application (add to Hide and Extra list with diferent names) or uninstall it?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (appListManager.getHidden().remove(app)) {
                    appListManager.save();
                    dialog.dismiss();
                }
            }
        });

        dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getName()));
                mainActivity.startActivity(intent);
                appListManager.reload();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!appListManager.getHidden().contains(app)) {
                    appListManager.getHidden().add(app);
                }
                app.setNick(dialogInput.getText().toString());
                appListManager.getExtra().add(app);
                appListManager.save();
                dialog.dismiss();
            }
        });
    }
}
