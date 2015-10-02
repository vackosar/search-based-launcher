package com.vackosar.searchbasedlauncher.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class App implements Comparable<App> {

    private final String name;
    private String nick;
    private final String activity;

    public App(String name, String nick, String activity) {
        this.name = name;
        this.nick = nick;
        this.activity = activity;
    }

    public App(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.name = jsonObject.getString("name");
            this.nick = jsonObject.getString("nick");
            this.activity = jsonObject.getString("activity");
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public App(App app) {
        this.name = app.getName();
        this.activity = app.getActivity();
        this.nick = app.getNick();
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

    private String getRecentNick() {
        return "R: " + nick;
    }

    public String getJsonString(){
        try {
            return new JSONObject()
                    .put("name", name)
                    .put("nick", nick)
                    .put("activity", activity)
                    .toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<App> getApps(Set<String> jsonSet) {
        final Set<App> set = new HashSet<>();
        for (String json: jsonSet) {
            set.add(new App(json));
        }
        return set;
    }

    public static Collection<String> getJson(Collection<App> set) {
        final Set<String> jsonSet = new HashSet<>();
        for (App app: set) {
            jsonSet.add(app.getJsonString());
        }
        return jsonSet;
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
