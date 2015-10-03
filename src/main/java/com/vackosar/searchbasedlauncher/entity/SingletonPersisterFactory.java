package com.vackosar.searchbasedlauncher.entity;

import android.content.Context;

import com.google.inject.Inject;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class SingletonPersisterFactory {
    @Inject Context context;
    public <T> SingletonPersister<T> create(T object) {
        return new SingletonPersister<>(object, context);
    }
}
