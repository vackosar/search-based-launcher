package com.vackosar.searchbasedlauncher.entity;

import com.google.gson.annotations.Expose;

public class App implements Comparable<App> {

    @Expose private final String name;
    @Expose private String nick;
    @Expose private final String activity;

    public App(String name, String nick, String activity) {
        this.name = name;
        this.nick = nick;
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public String getActivity() {
        return activity;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        return activity.equals(app.activity);

    }

    @Override
    public int hashCode() {
        return activity.hashCode();
    }

    @Override
    public int compareTo(App another) {
        if (another == null) {
            return nick.compareTo(null);
        }
        return nick.compareTo(another.getNick());
    }
}
