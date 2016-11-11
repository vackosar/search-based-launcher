package com.vackosar.searchbasedlauncher.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.inject.Inject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import roboguice.inject.ContextSingleton;

public class CrashReporter {


    private static final String TAG = CrashReporter.class.getName();

    public void reportPreviousCrashes(Context context){
        String line;
        String trace = "";
        File f = new File(context.getFilesDir().getPath() + "/stack.trace");
        if (f.exists()){
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(context
                                .openFileInput("stack.trace")));
                while((line = reader.readLine()) != null) {
                    trace += line+"\n";
                }
            } catch(FileNotFoundException fnfe) {
                Log.e(TAG, "crash report not found!");
                return;
            } catch(IOException ioe) {
                Log.e(TAG, "IO Exception.");
                return;

            }

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            String subject = "Error report";
            String body =  trace+
                            "\n\n";

            sendIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{});
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendIntent.setType("message/rfc822");

            context.startActivity(
                    Intent.createChooser(sendIntent, "Send crash log?"));

            context.deleteFile("stack.trace");
        }
    }



}
