package com.adminView;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class menuModificationRun extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(menuModificationRun.class.getResource("src/main/resources/adminView/manageMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 565, 676);
        stage.setTitle("Modify Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
