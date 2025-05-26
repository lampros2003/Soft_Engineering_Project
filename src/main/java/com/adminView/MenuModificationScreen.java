package com.adminView;

import com.common.Ingredient;
import com.mainpackage.SceneSwitching;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import com.menu.Menu;
import com.menu.MenuItem;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;

public class MenuModificationScreen {
    private ModifyMenuClass menuManager = new ModifyMenuClass();

    @FXML
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, String> dishColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, String> ingredientsColumn;

    @FXML
    private TableView<Ingredient> recommendedIngredientsTable;
    @FXML
    private TableColumn<Ingredient, String> ingredientName;
    @FXML
    private TableColumn<Ingredient, Integer> ingredientQuantity;

    public void initialize() {
        // Show the menu table
        Menu menu = menuManager.getCurrentMenu();

        dishColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.<MenuItem, Double>forTableColumn(new DoubleStringConverter()));
        ingredientsColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        menuTable.setEditable(true);
        displayMenu(menu);

        // Show the recommended ingredients table
        List<Ingredient> ingredient = menuManager.getRecommendedIngredients();

        ingredientName.setCellFactory(TextFieldTableCell.forTableColumn());
        ingredientQuantity.setCellFactory(TextFieldTableCell.<Ingredient, Integer>forTableColumn(new IntegerStringConverter()));

        recommendedIngredientsTable.setEditable(true);
        displayRecommendedIngredients(ingredient);

    }

    private void displayRecommendedIngredients(List<Ingredient> ingredients) {
        ObservableList<Ingredient> data = FXCollections.observableArrayList(ingredients);

        ingredientName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        ingredientQuantity.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());

        recommendedIngredientsTable.setItems(data);
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
