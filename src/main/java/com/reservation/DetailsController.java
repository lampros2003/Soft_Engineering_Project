package com.reservation;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DetailsController {

    // ── FXML-injected controls
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField telephoneField;
    @FXML private ComboBox<String> arrangementBox;

    // ── Initialisation
    @FXML
    public void initialize() {
        // Populate the arrangement choices once
        arrangementBox.setItems(FXCollections.observableArrayList(
                "Standard", "Window", "Outdoor", "Private Room"
        ));
        arrangementBox.getSelectionModel().selectFirst(); // default
    }

    // ── Navigation handlers
    @FXML
    public void handleBack(ActionEvent evt) {
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        Navigation.goTo("table_selection.fxml", stage);
    }

    @FXML
    public void handleNext(ActionEvent evt) {
        // read & validate -
        String first  = firstNameField.getText().trim();
        String last   = lastNameField.getText().trim();
        String phone  = telephoneField.getText().trim();
        String arrang = arrangementBox.getValue();

        if (first.isEmpty() || last.isEmpty() || phone.isEmpty()) {
            ErrorPage.display("Please fill in first name, last name and telephone.");
            return;
        }
        if (!phone.matches("\\d{7,15}")) {                           // simple check
            ErrorPage.display("Telephone number should contain 7-15 digits.");
            return;
        }

        // store for later DB insert / confirmation -
        ReservationDateService.setCustomerInfo(first, last, phone, arrang);

        // go to confirmation
        Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        Navigation.goTo("reservation_confirmation.fxml", stage);
    }
}

