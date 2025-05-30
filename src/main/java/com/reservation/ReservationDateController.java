package com.reservation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ReservationDateController {

    @FXML private DatePicker datePicker;
    @FXML private TextField peopleField;

    private final ReservationDateService dateService = new ReservationDateService();

    @FXML
    public void initialize() {
        try {
            YearMonth currentMonth = YearMonth.now();
            List<LocalDate> availableDates = new ArrayList<LocalDate>();

            /*dateService.getAvailableDates(currentMonth);*/

            datePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
//                    setDisable(empty || !availableDates.contains(item));
                }
            });

        } catch (Exception e) {
            ErrorPage.display("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void commitAndContinue(Stage currentStage) {
        LocalDate selectedDate = datePicker.getValue();
        int numberOfPeople;

        try {
            numberOfPeople = Integer.parseInt(peopleField.getText().trim());
        } catch (NumberFormatException nfe) {
            ErrorPage.display("Number of people must be a whole number.");
            return;
        }

        if (  numberOfPeople <= 0) {
            ErrorPage.display("Please select a valid date and enter a positive number of people.");
            return;
        }

        // persist the user’s choices so later screens can read them
        ReservationDateService.setReservationDate(selectedDate);
        ReservationDateService.setNumberOfPeople(numberOfPeople);

        // move forward to the time-selection step
        Navigation.goTo("/reservation/reservation_time.fxml", currentStage);
    }

    @FXML
    public void handleOkButton(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource())
                .getScene()
                .getWindow();
        commitAndContinue(stage);
    }
}
