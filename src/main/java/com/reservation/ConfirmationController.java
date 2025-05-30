package com.reservation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


public class ConfirmationController {

    // labels injected from the FXML
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;
    @FXML private Label tableLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label phoneLabel;
    @FXML private Label arrangementLabel;
    @FXML private Label peopleLabel;

    /* ───────────────────────────── initialise view ────────────────────────── */
    @FXML
    public void initialize() {
        // pull the data that previous steps stored in the service classes
        dateLabel.setText(
                ReservationDateService.getReservationDate()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));

        timeLabel.setText(
                ReservationDateService.getReservationTime()
                        .format(DateTimeFormatter.ofPattern("HH:mm")));

        tableLabel.setText(String.valueOf(ReservationDateService.getTableNumber()));
        peopleLabel.setText(String.valueOf(ReservationDateService.getNumberOfPeople()));

        firstNameLabel.setText(ReservationDateService.getFirstName());
        lastNameLabel.setText(ReservationDateService.getLastName());
        phoneLabel.setText(ReservationDateService.getTelephoneNumber());
        arrangementLabel.setText(ReservationDateService.getArrangement());
    }

    /* ───────────────────────────── navigation buttons ─────────────────────── */

    /** go back to the details screen so the guest can edit the data */
    @FXML
    public void handleBack(ActionEvent evt) {
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        Navigation.goTo("reservation_details.fxml", stage);
    }

    /** final step – write to DB and show confirmation / error */
    @FXML
    public void handleConfirm(ActionEvent evt) {
        try {
            // persist using your DAO / service layer
            boolean ok = ReservationDateService.saveReservation();
            if (!ok) {
                ErrorPage.display("Could not save reservation ¬– please try again.");
                return;
            }

            // show a simple “success” screen or close the wizard
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            Navigation.goTo("success.fxml", stage);

        } catch (Exception ex) {
            ErrorPage.display("Database error: " + ex.getMessage());
        }
    }
}

