package com.ideasfrombrain.search_based_launcher_v3;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PreferencesAdapter {
    public static final String SIZE = "size";
    final MainActivity mainActivity;
    final Context context;

    public PreferencesAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.context = mainActivity.getApplicationContext();
    }

    public boolean saveSet(Collection<App> set, String listName) {
        SharedPreferences prefs = context.getSharedPreferences(listName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        putStrings(editor, listName, App.getJson(set));
        return editor.commit();
    }

    private void putStrings(SharedPreferences.Editor editor, String name, Collection<String> strings) {
        final Iterator<String> iterator = strings.iterator();
        int index = -1;
        editor.putString(SIZE, String.valueOf(strings.size()));
        while (iterator.hasNext()) {
            String string = iterator.next();
            index++;
            editor.putString(String.valueOf(index), string);
        }
    }

    private Set<String> getStrings(String name) {
        SharedPreferences prefs = context.getSharedPreferences(name, 0);
        final String sizeString = prefs.getString(SIZE, null);
        if (sizeString == null) {
            return null;
        }
        int size = Integer.valueOf(sizeString);
        final Set<String> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            final String string = prefs.getString(String.valueOf(i), null);
            set.add(string);
        }
        return set;
    }

    public Set<App> loadSet(String listName) throws JSONException {
        return App.getApps(getStrings(listName));
    }
}
