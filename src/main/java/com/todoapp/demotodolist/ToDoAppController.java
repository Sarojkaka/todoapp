package com.todoapp.demotodolist;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.print.PrinterJob;
import javafx.scene.transform.Scale;
import javafx.scene.Node;

public class ToDoAppController {
    private ToDoListController toDoListController;

    @FXML
    private TextField inputField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> hourComboBox;

    @FXML
    private ComboBox<String> minuteComboBox;

    @FXML
    private ListView<ToDoItem> listView;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button printButton; // New print button

    @FXML
    private CheckBox doneCheckbox;

    private ToDoItem selectedItem;

    public void initialize() {
        // Initialize the time combo boxes with hours and minutes
        hourComboBox.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minuteComboBox.getItems().addAll("00", "15", "30", "45");

        // Set up the format for displaying date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ToDoList toDoList = new ToDoList();
        toDoListController = new ToDoListController(toDoList);
        listView.setItems(toDoListController.getObservableItems());

        // Set up list view cell factory to display ToDoItems
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ToDoItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        // Listen for selection changes and update input fields
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedItem = newSelection;
                inputField.setText(selectedItem.getDescription());
                LocalDateTime dateTime = selectedItem.getDateTime();
                datePicker.setValue(dateTime.toLocalDate());
                hourComboBox.setValue(dateTime.format(DateTimeFormatter.ofPattern("HH")));
                minuteComboBox.setValue(dateTime.format(DateTimeFormatter.ofPattern("mm")));
                doneCheckbox.setSelected(selectedItem.isDone());
            }
        });

        // Add listener for list changes to update print button availability
        listView.getItems().addListener((ListChangeListener<ToDoItem>) c -> {
            printButton.setDisable(listView.getItems().isEmpty());
        });

        // Disable print button initially if list is empty
        printButton.setDisable(listView.getItems().isEmpty());
    }

    @FXML
    protected void handleAddButtonAction() {
        String input = inputField.getText();
        LocalDate date = datePicker.getValue();
        String hour = hourComboBox.getValue();
        String minute = minuteComboBox.getValue();

        if (!input.isEmpty() && date != null && hour != null && minute != null) {
            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute)));
            toDoListController.addItem(input, dateTime);
            inputField.clear();
            datePicker.setValue(LocalDate.now());
            hourComboBox.setValue(null);
            minuteComboBox.setValue(null);
        }
    }

    @FXML
    protected void handleEditButtonAction() {
        if (selectedItem != null) {
            String input = inputField.getText();
            LocalDate date = datePicker.getValue();
            String hour = hourComboBox.getValue();
            String minute = minuteComboBox.getValue();
            boolean isDone = doneCheckbox.isSelected();

            if (!input.isEmpty() && date != null && hour != null && minute != null) {
                LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute)));
                selectedItem.setDescription(input);
                selectedItem.setDateTime(dateTime);
                selectedItem.setDone(isDone);

                // Refresh the list view to reflect changes
                listView.refresh();
            }
        }
    }

    @FXML
    protected void handleDeleteButtonAction() {
        if (selectedItem != null) {
            toDoListController.removeItem(selectedItem);
            inputField.clear();
            datePicker.setValue(LocalDate.now());
            hourComboBox.setValue(null);
            minuteComboBox.setValue(null);
            doneCheckbox.setSelected(false);
        }
    }

    @FXML
    protected void handleDoneCheckboxAction() {
        if (selectedItem != null) {
            selectedItem.setDone(doneCheckbox.isSelected());
            listView.refresh(); // Update the list view
        }
    }

    @FXML
    protected void handlePrintButtonAction() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean proceed = job.showPrintDialog(null);
            if (proceed) {
                printNode(job, listView);
                job.endJob();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Print Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not create a printer job.");
            alert.show();
        }
    }

    private void printNode(PrinterJob job, Node node) {
        // Scale the node to fit within the printable area
        double scaleX = job.getJobSettings().getPageLayout().getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY = job.getJobSettings().getPageLayout().getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        // Print the node
        boolean printed = job.printPage(node);
        if (printed) {
            job.endJob();
        }

        // Remove the scale transformation
        node.getTransforms().remove(scale);
    }
}
