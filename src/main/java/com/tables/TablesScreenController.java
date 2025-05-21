package com.tables;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TablesScreenController implements Initializable {

    @FXML private Pane table1, table2, table3, table4, table5, table6, table7, table8, table9, table10, table11, table12;
    
    private Pane currentlySelectedTable = null;
    private final Color SELECTED_COLOR = Color.web("#F39C12"); // Orange for selected table
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green for available tables
    private final List<Pane> availableTables = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the list of available tables
        availableTables.add(table6);
        availableTables.add(table8);
        availableTables.add(table9);
        
        // Set appropriate event handlers for available tables
        for (Pane table : availableTables) {
            table.setOnMouseEntered(e -> {
                if (table != currentlySelectedTable) {
                    Rectangle rect = (Rectangle) table.getChildren().get(table.getChildren().size() - 1);
                    rect.setOpacity(0.8); // Hover effect
                }
            });
            
            table.setOnMouseExited(e -> {
                if (table != currentlySelectedTable) {
                    Rectangle rect = (Rectangle) table.getChildren().get(table.getChildren().size() - 1);
                    rect.setOpacity(1.0); // Remove hover effect
                }
            });
        }
    }
    
    /**
     * Handles the table selection event
     */
    @FXML
    public void onTableSelected(MouseEvent event) {
        Pane selectedTable = (Pane) event.getSource();
        
        // Only allow selecting available tables
        if (!availableTables.contains(selectedTable)) {
            return;
        }
        
        // Deselect previously selected table
        if (currentlySelectedTable != null) {
            Rectangle rect = (Rectangle) currentlySelectedTable.getChildren()
                    .get(currentlySelectedTable.getChildren().size() - 1);
            rect.setFill(AVAILABLE_COLOR);
        }
        
        // Select new table
        Rectangle rect = (Rectangle) selectedTable.getChildren().get(selectedTable.getChildren().size() - 1);
        rect.setFill(SELECTED_COLOR);
        currentlySelectedTable = selectedTable;
        
        // Print table ID for debugging
        System.out.println("Selected table: " + selectedTable.getId());
    }
    
    /**
     * Handles the back button click
     */
    @FXML
    public void onBackButtonClicked(ActionEvent event) {
        // Close the window or navigate back
        System.out.println("Back button clicked");
        
        // Just close the window in this example
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        
        // In a real application, you would navigate back to the previous screen
        // For example: mainController.showPreviousScreen();
    }
    
    /**
     * Returns the currently selected table
     */
    public Pane getSelectedTable() {
        return currentlySelectedTable;
    }
}
