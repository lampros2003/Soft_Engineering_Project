package com.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManageMenuClass {
    ManageRequestStatusClass requestManager;
    public void callWaiter() {
        try {
            // Create an instance of ManageRequestStatusClass
            requestManager = new ManageRequestStatusClass();

            if (!requestManager.checkIfRequested()) {
                requestManager.callWaiter();
                openRequestStatusWindow(requestManager);
            } else {
                System.out.println("Υπάρχει ήδη ενεργό αίτημα.");
            }

        } catch (Exception e) {
            System.out.println("Σφάλμα στην κλήση σερβιτόρου");
            System.out.println(e);
//            ErrorNotificationScreen.showErrorScreen("Αποτυχία αποστολής αιτήματος.");
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

}
