package com.vackosar.searchbasedlauncher;

import javax.inject.Singleton;

import dagger.Provides;

@dagger.Module(
        injects = {
                AutostartButton.class,
                MainActivity.class,
                AppsView.class,
                DialogFactory.class,
                PreferencesAdapter.class,
                AppTypeSelector.class,
                SearchText.class,
                AppsManager.class,
                AppTypeSelector.class,
                MenuButton.class
        }
)
public class Module {
    final MainActivity mainActivity;

    public Module(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @Singleton
    MainActivity mainActivity() {
        return mainActivity;
    }

    @Provides
    @Singleton
    AutostartButton autostartButton(PreferencesAdapter preferencesAdapter) {
        return new AutostartButton(mainActivity, preferencesAdapter);
    }

    @Provides
    @Singleton
    PreferencesAdapter preferencesAdapter() {
        return new PreferencesAdapter(mainActivity);
    }

    @Provides
    @Singleton
    AppsView appsView(PreferencesAdapter preferencesAdapter, AutostartButton autostartButton, DialogFactory dialogFactory, SearchText searchText, AppsManager appsManager, AppTypeSelector appTypeSelector) {
        return new AppsView(mainActivity, preferencesAdapter, autostartButton, dialogFactory, searchText, appsManager, appTypeSelector);
    }

    @Provides
    @Singleton
    DialogFactory dialogFactory(AppTypeSelector appTypeSelector) {
        return new DialogFactory(mainActivity, appTypeSelector);
    }

    @Provides
    @Singleton
    AppTypeSelector appTypeSelector(PreferencesAdapter preferencesAdapter) {
        return new AppTypeSelector(mainActivity, preferencesAdapter);
    }

    @Provides
    @Singleton
    AppsManager appsManager(AppTypeSelector appTypeSelector, PreferencesAdapter preferencesAdapter) {
        return new AppsManager(mainActivity, appTypeSelector, preferencesAdapter);
    }

    @Provides
    @Singleton
    SearchText searchText() {
        return new SearchText(mainActivity);
    }

    @Provides
    @Singleton
    MenuButton menuButton(SearchText searchText, AppsManager appsManager, AppTypeSelector appTypeSelector) {
        return new MenuButton(mainActivity, searchText, appsManager, appTypeSelector);
    }
}
