package com.login;

import com.mainpackage.SceneSwitching;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML
    private void backToMain(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/mainUI/MainScreen.fxml");
    }

    @FXML
    private void loginAsWaiter(ActionEvent event) {
        System.out.println("loginAsWaiter");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/waiter/TABLES_PAGE.fxml");
    }

    @FXML
    private void loginAsAdministrator(ActionEvent event) {
        System.out.println("loginAsAdministrator");
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        SceneSwitching.switchScene(stage, "/adminUI/AdminScreen.fxml");
    }

    @FXML
    private void loginAsFrontDesk(ActionEvent event) {
        System.out.println("loginAsFrontDesk");
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        SceneSwitching.switchScene(stage, "/fontdeskUI/FrontDescScreen.fxml");
    }

    @FXML
    private void loginAsChef(ActionEvent event) {
        System.out.println("loginAsChef");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/chefUI/ChefScreen.fxml");
    }
}
