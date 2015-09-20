package com.vackosar.searchbasedlauncher.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class PreferencesAdapter {

    @Inject private Context context;

    public static final String SIZE = "size";

    public <T> boolean save(String name, T o) {
        final SharedPreferences.Editor editor = getEditor(name);
        if (o instanceof Boolean) {
            editor.putBoolean(name, (Boolean) o);
        } else if (o instanceof String) {
            editor.putString(name, (String) o);
        } else {
            throw new IllegalArgumentException("Unsupported type " + o.getClass() + "");
        }
        return editor.commit();
    }

    public <T> T load(String name, Class<T> clazz) {
        final SharedPreferences preferences = getSharedPreferences(name);
        if (clazz.equals(Boolean.class)) {
            return clazz.cast(preferences.getBoolean(name, true));
        } else if (clazz.equals(String.class)) {
            return clazz.cast(preferences.getString(name, null));
        } else if (clazz.equals(Integer.class)) {
            return clazz.cast(preferences.getInt(name, 0));
        } else {
            throw new IllegalArgumentException("Unsupported type " + clazz + "");
        }
    }

    public boolean saveSet(Collection<App> set, String name) {
        SharedPreferences.Editor editor = getEditor(name);
        putStrings(editor, App.getJson(set));
        return editor.commit();
    }

    private SharedPreferences.Editor getEditor(String name) {
        SharedPreferences prefs = getSharedPreferences(name);
        return prefs.edit();
    }

    private SharedPreferences getSharedPreferences(String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private void putStrings(SharedPreferences.Editor editor, Collection<String> strings) {
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
        SharedPreferences prefs = getSharedPreferences(name);
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

    public Set<App> loadSet(String listName) {
        try {
            return App.getApps(getStrings(listName));
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
}
