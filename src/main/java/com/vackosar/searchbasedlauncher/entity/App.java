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

        if (name != null ? !name.equals(app.name) : app.name != null) return false;
        return activity != null ? activity.equals(app.activity) : app.activity == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (activity != null ? activity.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(App another) {
        if (another == null) {
            return nick.compareTo(null);
        }
        return nick.compareTo(another.getNick());
    }
}
