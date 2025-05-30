// ErrorPageController.java
package com.reservation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorPageController {

    @FXML private Label  messageLabel;
    @FXML private Button backButton;

    /** put the message coming from ErrorPage.display(...) onto the label */
    @FXML
    public void initialize() {
        messageLabel.setText(ErrorPage.getMessage());   // :contentReference[oaicite:0]{index=0}
    }

    /** close the pop-up = > main window underneath becomes active again */
    @FXML
    private void onBackClick() {
        Stage popup = (Stage) backButton.getScene().getWindow();
        popup.close();
    }
}

