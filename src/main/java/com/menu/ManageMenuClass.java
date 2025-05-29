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

    // public void checkIfRequested(){}
    // public void startCountingTime(){}
    // public void stopCountingTime(){}

    public void callWaiter() {
        try {
            // Create an instance of ManageRequestStatusClass
            requestManager = new ManageRequestStatusClass(this.tableNumber);

//            if (!requestManager.checkIfRequested()) {
//                requestManager.createNewRequest();
//                requestManager.callWaiter();
//                showRequestStatus(requestManager);
//            } else {
//                showAlreadyRequestedError(requestManager);
//            }

        } catch (Exception e) {
            System.out.println("Σφάλμα στην κλήση σερβιτόρου: " + e.getMessage());
        }
    }

    private void showAlreadyRequestedError(ManageRequestStatusClass requestManager) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/errorHandling/alreadyRequested.fxml"));
        Parent root = loader.load();

        RequestStatusWindow controller = loader.getController();
        controller.setManager(requestManager);

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
