package com.todoapp.demotodolist;

import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private List<ToDoItem> items;

    public ToDoList() {
        this.items = new ArrayList<>();
    }

    public void addItem(ToDoItem item) {
        items.add(item);
    }

    public void removeItem(ToDoItem item) {
        items.remove(item);
    }

    public List<ToDoItem> getItems() {
        return items;
    }
}
