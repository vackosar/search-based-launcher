package com.vackosar.searchbasedlauncher.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class SingletonPersister<T> {

    private static final String KEY = "json";
    private final Context context;
    private final Class<? extends T> clazz;
    private final T singletonInstance;
    private final Gson gson;
    private InstanceCreator<T> instanceCreator;

    public SingletonPersister(final T singletonInstance, final Context context) {
        this.singletonInstance = singletonInstance;
        this.context = context;
        this.clazz = (Class<? extends T>) singletonInstance.getClass();
        instanceCreator = createInstanceCreator(singletonInstance);
        gson = createGson();

    }

    private Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(clazz, instanceCreator)
                .create();
    }

    private InstanceCreator<T> createInstanceCreator(final T singletonInstance) {
        return new InstanceCreator<T>() {

            @Override
            public T createInstance(Type type) {
                return singletonInstance;
            }
        };
    }

    public void save() {
        final String json = gson.toJson(singletonInstance);
        getEditor().putString(KEY, json).commit();
    }

    public void load() {
        final String json = getSharedPreferences().getString(KEY, null);
        if (json != null) {
            gson.fromJson(json, clazz);
        }
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.edit();
    }

    private SharedPreferences getSharedPreferences() {
        final String preferencesName = clazz.getCanonicalName();
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

}
