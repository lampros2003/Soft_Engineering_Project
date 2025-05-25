package com.waiter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class waiter_app extends Application {
    @Override
    public void start(Stage primaryStage) {



        Parent root;
        try{
            root = FXMLLoader.load(getClass().getResource("/waiter/TABLES_PAGE.fxml"));
            Scene scene = new Scene(root);


            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(Exception e){
            System.out.println(e);;}


    }

    public static void main(String[] args) {
        launch(args);
    }
}
