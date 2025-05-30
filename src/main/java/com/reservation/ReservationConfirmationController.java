package com.reservation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReservationConfirmationController {

    @FXML private TextArea summaryTextArea;

    @FXML
    public void initialize() {
        summaryTextArea.setText(ReservationDateService.getSummaryText());
    }

    @FXML
    private void onConfirmClick() {
        try {
            ReservationDateService.saveReservation();
            ReservationDateService.clear(); // reset data
            Navigation.goTo("reservation_date.fxml", (Stage) summaryTextArea.getScene().getWindow());

        } catch (Exception e) {
            ErrorPage.display("Could not save reservation: " + e.getMessage());
        }
    }
}
