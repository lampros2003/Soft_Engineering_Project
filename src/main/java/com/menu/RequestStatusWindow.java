package com.menu;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class RequestStatusWindow {

    @FXML
    private VBox messagesBox;

    private final ManageRequestStatusClass manager = new ManageRequestStatusClass();

    @FXML
    public void initialize() {
        manager.initialize();
        addMessage("Request sent to waiter...");
    }

    public void addMessage(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 8 12; -fx-background-radius: 10;");
        messagesBox.getChildren().add(label);
    }

    @FXML
    public void handleAccept() {
        manager.accept();
        addMessage("Waiter accepted the request.");
    }

    @FXML
    public void handleArrived() {
        manager.arrived();
        addMessage("Waiter arrived at your table.");
    }

    @FXML
    public void handleCancel() {
        manager.cancel();
        addMessage("Request canceled.");
    }


}

