package com.todoapp.demotodolist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToDoItem {
    private String description;
    private LocalDateTime dateTime;
    private boolean done;

    public ToDoItem(String description, LocalDateTime dateTime) {
        this.description = description;
        this.dateTime = dateTime;
        this.done = false;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return description + " (Due: " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ")" + (done ? " [Done]" : "");
    }
}
