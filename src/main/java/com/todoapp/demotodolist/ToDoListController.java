package com.todoapp.demotodolist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class ToDoListController {
    private ToDoList toDoList;
    private ObservableList<ToDoItem> observableItems;

    public ToDoListController(ToDoList toDoList) {
        this.toDoList = toDoList;
        this.observableItems = FXCollections.observableArrayList(toDoList.getItems());
    }

    public void addItem(String description, LocalDateTime dateTime) {
        ToDoItem item = new ToDoItem(description, dateTime);
        toDoList.addItem(item);
        observableItems.add(item);
    }

    public void removeItem(ToDoItem item) {
        toDoList.removeItem(item);
        observableItems.remove(item);
    }

    public ObservableList<ToDoItem> getObservableItems() {
        return observableItems;
    }
}
