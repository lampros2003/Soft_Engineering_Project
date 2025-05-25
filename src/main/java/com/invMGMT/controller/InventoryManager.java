package com.invMGMT.controller;

import com.mainpackage.SceneSwitching;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class InventoryManager {
    Stage stage;
    FXMLLoader loader;
    void init(Stage stage){
        this.stage=stage;
        this.loader= SceneSwitching.switchSceneR(stage,"/view/invMGMT/invMGMT.fxml");
    }
}
