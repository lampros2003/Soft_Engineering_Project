package com.adminView;

import com.mainpackage.SceneSwitching;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.menu.Menu;
import com.menu.MenuItem;

public class MenuModificationScreen {
    private ModifyMenuClass menuManager = new ModifyMenuClass();

    @FXML private TableView<MenuItem> menuTable;
    @FXML private TableColumn<MenuItem, String> dishColumn;
    @FXML private TableColumn<MenuItem, Double> priceColumn;
    @FXML private TableColumn<MenuItem, String> ingredientsColumn;

    public void initialize() {
        Menu menu = menuManager.getCurrentMenu();
        displayMenu(menu);
    }

    private void displayMenu(Menu menu) {
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(menu.getItems());

        dishColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        ingredientsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIngredients()));

        menuTable.setItems(menuItems);
    }

    @FXML
    private void onModifyMenu(ActionEvent event) {
        System.out.println("Modify Menu button clicked");
    }

    @FXML
    private void redirectToDashboard(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/adminView/dummyDashboard.fxml");
    }
}
