package com.reservation;

import com.tables.RestaurantLayout;
import com.tables.TableStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;


public class TableSelectionController {

    @FXML private ListView<TableStatus> tableListView;
    @FXML private Label messageLabel;

    private final ReservationDateService dateService = new ReservationDateService();

    @FXML
    public void initialize() {
        try {
            List<TableStatus> available = dateService.getAvailableTables(
                    ReservationDateService.getNumberOfPeople());

            if (available.isEmpty()) {
                messageLabel.setText("No tables available for the selected date/time.");
                tableListView.setDisable(true);
            } else {
                tableListView.setItems(FXCollections.observableArrayList(available));
                // Nice-to-have: show capacity in the cell text
                tableListView.setCellFactory(lv -> new ListCell<>() {
                    @Override protected void updateItem(TableStatus item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("Table " + item.getId() +
                                    " – seats " + item.getSeatingCapacity());
                        }
                    }
                });
            }
        } catch (Exception ex) {
            ErrorPage.display("Unable to load tables: " + ex.getMessage());
        }
    }

    @FXML
    private void onNextClick() {
        TableStatus chosen = tableListView.getSelectionModel().getSelectedItem();
        if (chosen == null) {
            ErrorPage.display("Please select a table first.");
            return;
        }
        //ReservationDateService.setReservationTableId(chosen.getId());
        Navigation.goTo("reservation_details.fxml",
                (Stage) tableListView.getScene().getWindow());
    }

    @FXML
    private void onBackClick() {
        Navigation.goTo("reservation_time.fxml",
                (Stage) tableListView.getScene().getWindow());
    }
}
