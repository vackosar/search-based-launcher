package com.vackosar.searchbasedlauncher.boundary;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.inject.Inject;
import com.vackosar.searchbasedlauncher.entity.Indentifiable;

import java.lang.reflect.Type;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class SingletonPersister<T extends Indentifiable> {

    @Inject private Context context;
    private static final String KEY = "json";

    public SingletonPersister() {}

    public SingletonPersister(Context context) {
        this.context = context;
    }

    private Gson createGson(T singleton) {
        requireNonNull(singleton);
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(singleton.getClass(), createInstanceCreator(singleton))
                .create();
    }

    private void requireNonNull(T singleton) {
        if (singleton == null) {
            throw new IllegalArgumentException("Cannot save null objects.");
        }
    }

    private InstanceCreator<T> createInstanceCreator(final T singletonInstance) {
        return new InstanceCreator<T>() {
            @Override
            public T createInstance(Type type) {
                return singletonInstance;
            }
        };
    }
    
    public void save(T singleton) {
        final String json = jsonfy(singleton);
        getEditor(singleton).putString(KEY, json).commit();
    }

    private String jsonfy(T singleton) {
        return createGson(singleton).toJson(singleton);
    }

    public void load(T singleton) {
        requireNonNull(singleton);
        final String json = getSharedPreferences(singleton).getString(KEY, null);
        if (json != null) {
            try {
                dejsonfy(singleton, json);
            } catch (Exception e) {
                save(singleton);
            }
        } else {
            save(singleton);
        }
    }

    private void dejsonfy(T singleton, String json) {
        createGson(singleton).fromJson(json, singleton.getClass());
    }

    private SharedPreferences.Editor getEditor(T singleton) {
        SharedPreferences prefs = getSharedPreferences(singleton);
        return prefs.edit();
    }

    private SharedPreferences getSharedPreferences(T singleton) {
        final String preferencesName = singleton.getId();
        return context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

}
