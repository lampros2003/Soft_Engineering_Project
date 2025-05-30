package com.menu;

import com.callWaiter.ManageRequestStatusClass;
import com.callWaiter.RequestStatusWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageMenuClass {
    ManageRequestStatusClass requestManager;
    private int tableNumber = 1; // Example table number, replace with actual value

    public ManageMenuClass(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void callWaiter() {
        try {
            requestManager = new ManageRequestStatusClass(1);

            if (!requestManager.checkIfRequested()) {
                requestManager.initializeStatus();

                System.out.println("Calling waiter...");
                requestManager.callWaiter();

                System.out.println("Showing request status...");
                showRequestStatus(requestManager);
            } else {
                System.out.println("Request already active. Showing error...");
                showAlreadyRequestedError(requestManager);
            }

        } catch (Exception e) {
            System.out.println("Error in callWaiter method: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace for debugging
        }
    }

    private void showAlreadyRequestedError(ManageRequestStatusClass requestManager) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/error/alreadyRequestedError.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Request Status");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showRequestStatus(ManageRequestStatusClass requestManager) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/RequestStatusWindow.fxml"));
        Parent root = loader.load();

        RequestStatusWindow controller = loader.getController();
        controller.setManager(requestManager);

        Stage stage = new Stage();
        stage.setTitle("Request Status");
        stage.setScene(new Scene(root));
        stage.show();
    }

}