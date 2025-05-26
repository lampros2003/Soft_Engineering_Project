package com.adminView;

import com.mainpackage.SceneSwitching;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class MenuModificationScreen {
    @FXML private TableView<MenuItem> menuTable;
    @FXML private TableColumn dishColumn;
    @FXML private TableColumn priceColumn;
    @FXML private ListView<String> recommendedList;
    @FXML private ListView<String> expiredList;
//    private final ManageMenuClass manage = new ManageMenuClass();
//    private ObservableList<MenuItem> menuItems;

    public MenuModificationScreen(ListView<String> recommendedList) {
        this.recommendedList = recommendedList;
    }

//    @FXML
//    public void initialize() {
//        Menu data = manage.displayMenu();
//        menuItems = FXCollections.observableArrayList(data.getItems());
//        menuTable.setItems(menuItems);
//
//        dishColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
//        priceColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()).asObject());
//
//        menuTable.setEditable(true);
//        dishColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        dishColumn.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));
//        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
//        priceColumn.setOnEditCommit(e -> e.getRowValue().setPrice(e.getNewValue()));
//
//        recommendedList.setItems(FXCollections.observableArrayList(manage.getRecommendedIngredients()));
//        expiredList.setItems(FXCollections.observableArrayList(manage.getExpiredOffers()));
//    }
//
//    @FXML
//    private void onModifyMenu() {
//        com.menu.Menu updatedMenu = new Menu(new ArrayList<>(menuItems));
//        if (!manage.checkIfChangesAreValid(updatedMenu)) {
//            Alert error = new Alert(Alert.AlertType.ERROR, "Το μενού είναι άδειο!", ButtonType.OK);
//            error.show();
//            return;
//        }
//
//        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Να αποθηκευτούν οι αλλαγές;", ButtonType.OK, ButtonType.CANCEL);
//        Optional<ButtonType> result = confirm.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            boolean success = manage.modifyMenu(updatedMenu);
//            Alert info = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
//                    success ? "Αποθήκευση επιτυχής!" : "Αποτυχία αποθήκευσης!");
//            info.show();
//        }
//    }

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
