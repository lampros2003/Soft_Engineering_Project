package com.callWaiter;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class RequestStatusWindow {

    @FXML
    private VBox messagesBox;

    private ManageRequestStatusClass manager;

    public void setManager(ManageRequestStatusClass requestManager) {
        this.manager = requestManager;
        showRequestStatus();
    }

    @FXML
    public void showRequestStatus() {
        addMessage("Request sent to waiter...");
    }

    public void updateRequestStatusAccepted() {
        addMessage("Request accepted by waiter.");
    }

    public void updateRequestStatusArrived() {
        addMessage("Request arrived by waiter.");
    }

    public void cancelRequestStatus(){
        manager.cancelRequest();
    }

    public void addMessage(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 8 12; -fx-background-radius: 10;");
        messagesBox.getChildren().add(label);
    }
}

