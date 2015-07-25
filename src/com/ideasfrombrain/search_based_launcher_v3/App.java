package com.ideasfrombrain.search_based_launcher_v3;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class App {
    final String name;
    final String nick;
    final String activity;

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

    public String getName() {
        return name;
    }

    public String getActivity() {
        return activity;
    }


    public String getNick() {
        return nick;
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

    public static List<App> getAppList(List<String> jsonList) {
        final List<App> list = new ArrayList<>();
        for (String json: jsonList) {
            list.add(new App(json));
        }
        return list;
    }

    public static List<String> getJsonList(List<App> list) {
        final List<String> jsonList = new ArrayList<String>();
        for (App app: list) {
            jsonList.add(app.getJsonString());
        }
        return jsonList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        if (!name.equals(app.name)) return false;
        if (!nick.equals(app.nick)) return false;
        return activity.equals(app.activity);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + nick.hashCode();
        result = 31 * result + activity.hashCode();
        return result;
    }
}
