package com.ideasfrombrain.search_based_launcher_v3;

public class App {
    final String name;
    final String nick;
    final String activity;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        return !(activity != null ? !activity.equals(app.activity) : app.activity != null);

    }

    @Override
    public int hashCode() {
        return activity != null ? activity.hashCode() : 0;
    }
}
