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
    // Class to manage menu modifications
    private ModifyMenuClass menuManager = new ModifyMenuClass();

    // FXML components
    @FXML
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, String> dishColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, String> ingredientsColumn;
    @FXML
    private TableColumn<MenuItem, Double> discountColumn;
    @FXML
    private TableColumn<MenuItem, String> expirationColumn;

    @FXML
    private TableView<Ingredient> recommendedIngredientsTable;
    @FXML
    private TableColumn<Ingredient, String> ingredientName;
    @FXML
    private TableColumn<Ingredient, Integer> ingredientQuantity;

    public void initialize() {
        // Show the menu table
        Menu menu = menuManager.getCurrentMenu();

        // UI related commands, so they should be initialized in the initialize method
        // Set up the menu table columns
        dishColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ingredientsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.<MenuItem, Double>forTableColumn(new DoubleStringConverter()));

        // Administrator can modify only the price of the dishes
        menuTable.setEditable(true);

        displayMenu(menu);

        // Show the recommended ingredients table
        List<Ingredient> ingredient = menuManager.getRecommendedIngredients();

        ingredientName.setCellFactory(TextFieldTableCell.forTableColumn());
        ingredientQuantity.setCellFactory(TextFieldTableCell.<Ingredient, Integer>forTableColumn(new IntegerStringConverter()));

        displayRecommendedIngredients(ingredient);

        priceColumn.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setPrice(event.getNewValue());
        });

        discountColumn.setCellFactory(TextFieldTableCell.<MenuItem, Double>forTableColumn(new DoubleStringConverter()));
        discountColumn.setOnEditCommit(event -> {
            MenuItem item = event.getRowValue();
            item.setDiscount(event.getNewValue());
        });
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
    private void onAddMenu(ActionEvent event) {
        System.out.println("Add Menu button clicked");
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void onModifyMenu(ActionEvent event) {
        ObservableList<MenuItem> updatedItems = menuTable.getItems();

        for (MenuItem item : updatedItems) {
            System.out.println(item.getDiscount());
            if (!menuManager.checkIfChangesAreValid(item)) {
                showAlert("Invalid data in menu.");
                return;
            }
        }

        boolean successMenu = menuManager.updateMenuItems(updatedItems);
        boolean successDeals = menuManager.updateDeals(updatedItems);

        if (successMenu && successDeals) {
            showAlert("Menu and Deals updated successfully!");
        } else {
            showAlert("Something went wrong updating the database.");
        }
    }

    @FXML
    private void redirectToDashboard(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitching.switchScene(stage, "/adminView/dummyDashboard.fxml");
    }
}
