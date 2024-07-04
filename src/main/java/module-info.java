module com.todoapp.demotodolist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.todoapp.demotodolist to javafx.fxml;
    exports com.todoapp.demotodolist;
}