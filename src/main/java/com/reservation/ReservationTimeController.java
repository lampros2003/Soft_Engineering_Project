package com.reservation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.List;

public class ReservationTimeController {

    @FXML
    private Spinner<LocalTime> timeSpinner;

    private final ReservationTimeService timeService = new ReservationTimeService();


    @FXML
    public void initialize() {
        try {
            List<LocalTime> availableTimes = timeService.getAvailableTimes(
                    ReservationDateService.getReservationDate(),
                    ReservationDateService.getNumberOfPeople());

            if (availableTimes.isEmpty()) {
                ErrorPage.display("No available times for this date and number of people.");
                return;
            }

            // ListSpinnerValueFactory lets the user cycle through the list
            SpinnerValueFactory<LocalTime> valueFactory =
                    new SpinnerValueFactory.ListSpinnerValueFactory<>(
                            FXCollections.observableArrayList(availableTimes));

            // (optional) show the first free slot immediately
            valueFactory.setValue(availableTimes.get(0));

            timeSpinner.setValueFactory(valueFactory);

        } catch (Exception e) {
            ErrorPage.display("Database error: " + e.getMessage());
        }
    }




    @FXML




    public void handleBack(javafx.event.ActionEvent event) {
        Navigation.back(
                (Stage) ((javafx.scene.Node) event.getSource())
                        .getScene()
                        .getWindow()
        );
    }

    @FXML
    private void handleNext(ActionEvent evt) {
        LocalTime selectedTime = timeSpinner.getValue();   // instead of comboBox.getValue()
        if (selectedTime == null) {
            ErrorPage.display("Please select a time first.");
            return;
        }
        ReservationDateService.setReservationTime(selectedTime);
        Navigation.goTo("table_selection.fxml",
                (Stage) ((Node) evt.getSource()).getScene().getWindow());
    }


}


