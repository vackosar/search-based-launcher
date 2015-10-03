package com.vackosar.searchbasedlauncher.control;

import com.google.gson.Gson;
import com.vackosar.searchbasedlauncher.entity.App;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {
    public static final String JSON = "{\"name\":\"name\",\"nick\":\"nick\",\"activity\":\"activity\"}";
    public static final App APP = new App("name", "nick", "activity");
    Gson gson = new Gson();

    @Test
    public void jsonfy() {
        Assert.assertEquals(JSON, gson.toJson(APP));
    }

    @Test
    public void dejsonfy() {
        Assert.assertEquals(APP, gson.fromJson(JSON, App.class));
    }
}
