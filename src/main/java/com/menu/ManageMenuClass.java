package com.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class ManageMenuClass {
    ManageRequestStatusClass requestManager;
    private int tableNumber = 1; // Example table number, replace with actual value

    public ManageMenuClass(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void callWaiter(int tableNumber) {
        try {
            // Create an instance of ManageRequestStatusClass
            requestManager = new ManageRequestStatusClass(tableNumber);

            if (!requestManager.checkIfRequested()) {
                requestManager.callWaiter();
                openRequestStatusWindow(requestManager);
            } else {
                System.out.println("Υπάρχει ήδη ενεργό αίτημα.");
            }

        } catch (Exception e) {
            System.out.println("Σφάλμα στην κλήση σερβιτόρου: " + e.getMessage());
        }
    }

    private void openRequestStatusWindow(ManageRequestStatusClass requestManager) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu/RequestStatusWindow.fxml"));
        Parent root = loader.load();

        RequestStatusWindow controller = loader.getController();
        controller.setManager(requestManager);

        Stage stage = new Stage();
        stage.setTitle("Request Status");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public int getTableNumber() {
        return tableNumber;
    }

}
