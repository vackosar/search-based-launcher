package com.vackosar.searchbasedlauncher.entity;

public class Item implements Comparable<Item> {

    private final String name;
    private final Action action;

    public Item(String name, Action action) {
        this.name = name;
        this.action = action;
    }

    public void act() {
        action.act();
    }

    public String getId() {
        return action.getId();
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Item another) {
        return name.compareTo(another.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return !(action != null ? !action.equals(item.action) : item.action != null);

    }

    @Override
    public int hashCode() {
        return action != null ? action.hashCode() : 0;
    }
}
