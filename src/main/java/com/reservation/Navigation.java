package com.reservation;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class Navigation {

    /** LIFO stack of the scenes we already visited */
    private static final Deque<Scene> history = new ArrayDeque<>();

    /**
     * Loads {@code fxmlFile} and shows it on {@code stage}.
     * The current scene is pushed onto {@link #history} so we can pop it later.
     */
    public static void goTo(String fxmlFile, Stage stage) {
        try {
            // Ensure the path starts with “/reservation/”
            if (!fxmlFile.startsWith("/")) {
                fxmlFile = "/reservation/" + fxmlFile;
            }
            FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            ErrorPage.display("Navigation error: " + e.getMessage());
        }
    }


    /**
     * Pops the most recent scene off the stack and shows it.
     * If the stack is empty we simply do nothing.
     */
    public static void back(Stage stage) {
        if (!history.isEmpty()) {
            stage.setScene(history.pop());
            stage.show();
        }
    }
}
