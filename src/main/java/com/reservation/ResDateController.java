package com.reservation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ResDateController {

    @FXML
    private DatePicker datePicker;


    @FXML
    private void initialize() {

        // Disable past days
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-opacity: 0.4;");
                }
            }
        });
    }

    /** NEXT button handler */
    @FXML
    private void handleNext(ActionEvent event) {

        LocalDate selected = datePicker.getValue();

        /* -------- 1) Show warning if nothing chosen -------- */
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(datePicker.getScene().getWindow());
            alert.setHeaderText(null);
            alert.setContentText(
                    "No date selected. Please pick a day for your reservation."
            );
            alert.showAndWait();
            return;            // keep the user on the same screen
        }



        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/resources/reservation/reservation_time.fxml")
            ); // reservation_time.fxml must declare fx:controller="com.reservation.ResTimeController"

            Stage stage = (Stage) datePicker.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException ex) {
            ex.printStackTrace();  // replace with proper error handling
        }
    }
}
