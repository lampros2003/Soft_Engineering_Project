package com.tables;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TableLayoutEditorController implements Initializable {

    @FXML private GridPane editorGrid;
    @FXML private Spinner<Integer> seatingSpinner;
    @FXML private Spinner<Integer> widthSpinner;
    @FXML private Spinner<Integer> heightSpinner;
    @FXML private ComboBox<TablesScreenController.TableState> tableStateComboBox;
    @FXML private Button addTableButton;
    @FXML private Label selectedTableLabel;
    @FXML private Button removeTableButton;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    
    private RestaurantLayout workingLayout;
    private Map<Integer, Pane> tablePanes = new HashMap<>();
    private TableStatus selectedTable = null;
    
    // Colors for different table states
    private final Color OCCUPIED_COLOR = Color.web("#F39C12"); // Orange
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green
    private final Color UNAVAILABLE_COLOR = Color.web("#AAAAAA"); // Gray
    
    // Reference to the main controller to update when saving
    private TablesScreenController mainController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize spinners with value factories
        seatingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 4));
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(60, 200, 100, 10));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(60, 180, 100, 10));
        
        // Initialize table state combo box
        tableStateComboBox.setItems(FXCollections.observableArrayList(
            TablesScreenController.TableState.AVAILABLE,
            TablesScreenController.TableState.OCCUPIED,
            TablesScreenController.TableState.UNAVAILABLE
        ));
        tableStateComboBox.setValue(TablesScreenController.TableState.AVAILABLE);
    }
    
    /**
     * Set the main controller reference and initialize the working layout
     */
    public void setMainController(TablesScreenController controller) {
        this.mainController = controller;
        
        // Create a deep copy of the layout to work with
        this.workingLayout = cloneLayout(controller.getRestaurantLayout());
        
        // Render the tables
        renderTables();
    }
    
    /**
     * Create a deep copy of the layout to prevent modifying the original until save
     */
    private RestaurantLayout cloneLayout(RestaurantLayout original) {
        RestaurantLayout clone = new RestaurantLayout(
            original.getLayoutName(), 
            original.getDescription()
        );
        
        // Clone all tables
        for (TableStatus table : original.getAllTables()) {
            clone.addTable(
                table.getPosition(),
                table.getWidth(),
                table.getHeight(),
                table.getSeatingCapacity(),
                table.getState(),
                table.getColumnIndex(),
                table.getRowIndex()
            );
        }
        
        return clone;
    }
    
    /**
     * Render tables from the working layout
     */
    private void renderTables() {
        // Clear existing content in the grid
        editorGrid.getChildren().clear();
        tablePanes.clear();
        
        // Create and add tables to the grid
        for (TableStatus table : workingLayout.getAllTables()) {
            Pane tablePane = createTablePane(table);
            tablePanes.put(table.getId(), tablePane);
            
            // Add table to the grid at the specified position
            editorGrid.add(tablePane, table.getColumnIndex(), table.getRowIndex());
        }
    }
    
    /**
     * Create a table pane based on the table data for the editor
     */
    private Pane createTablePane(TableStatus table) {
        StackPane stackPane = new StackPane();
        
        // Create the main table pane
        Pane tablePane = new Pane();
        tablePane.setId("table" + table.getId());
        tablePane.setPrefWidth(table.getWidth());
        tablePane.setPrefHeight(table.getHeight());
        
        // Set style class based on status
        String styleClass = "table-unavailable";
        Color tableColor = UNAVAILABLE_COLOR;
        
        switch (table.getState()) {
            case AVAILABLE:
                styleClass = "table-available";
                tableColor = AVAILABLE_COLOR;
                break;
            case OCCUPIED:
                styleClass = "table-occupied";
                tableColor = OCCUPIED_COLOR;
                break;
            case UNAVAILABLE:
            default:
                styleClass = "table-unavailable";
                tableColor = UNAVAILABLE_COLOR;
                break;
        }
        
        tablePane.getStyleClass().add(styleClass);
        
        // Add table rectangle
        Rectangle tableRect = new Rectangle(table.getWidth(), table.getHeight());
        tableRect.setArcHeight(5.0);
        tableRect.setArcWidth(5.0);
        tableRect.setFill(tableColor);
        tablePane.getChildren().add(tableRect);
        
        // Add table number label
        Text tableNumber = new Text(String.valueOf(table.getId()));
        tableNumber.setFill(Color.WHITE);
        tableNumber.setStyle("-fx-font-weight: bold;");
        tableNumber.setTranslateX(table.getWidth() / 2 - 5);
        tableNumber.setTranslateY(table.getHeight() / 2 + 5);
        tablePane.getChildren().add(tableNumber);
        
        // Add capacity text
        Text capacityText = new Text("Seats: " + table.getSeatingCapacity());
        capacityText.setFill(Color.WHITE);
        capacityText.setStyle("-fx-font-size: 10px;");
        capacityText.setTranslateX(table.getWidth() / 2 - 20);
        capacityText.setTranslateY(table.getHeight() / 2 + 20);
        tablePane.getChildren().add(capacityText);
        
        // Add selection event handler
        tablePane.setOnMouseClicked(e -> selectTable(table));
        
        // Add drag-and-drop functionality
        makeDraggable(tablePane, table);
        
        stackPane.getChildren().add(tablePane);
        return stackPane;
    }
    
    /**
     * Make a table pane draggable for repositioning
     */
    private void makeDraggable(Pane tablePane, TableStatus table) {
        final Delta dragDelta = new Delta();
        
        tablePane.setOnMousePressed(mouseEvent -> {
            // Record the starting point for the drag
            dragDelta.x = tablePane.getLayoutX() - mouseEvent.getSceneX();
            dragDelta.y = tablePane.getLayoutY() - mouseEvent.getSceneY();
            tablePane.setCursor(javafx.scene.Cursor.MOVE);
            mouseEvent.consume();
        });
        
        tablePane.setOnMouseReleased(mouseEvent -> {
            tablePane.setCursor(javafx.scene.Cursor.HAND);
            
            // Calculate the new grid position
            int columnIndex = getColumnIndex(mouseEvent.getSceneX());
            int rowIndex = getRowIndex(mouseEvent.getSceneY());
            
            // Update the table position in the layout
            workingLayout.moveTable(table.getId(), columnIndex, rowIndex);
            
            // Re-render all tables to reflect the new positions
            renderTables();
            
            mouseEvent.consume();
        });
        
        tablePane.setOnMouseDragged(mouseEvent -> {
            // Show the table being dragged
            tablePane.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            tablePane.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            mouseEvent.consume();
        });
        
        tablePane.setOnMouseEntered(mouseEvent -> {
            tablePane.setCursor(javafx.scene.Cursor.HAND);
            mouseEvent.consume();
        });
    }
    
    /**
     * Calculate the column index based on x position
     */
    private int getColumnIndex(double x) {
        // Simple calculation - can be refined based on your grid
        int columnWidth = 150; // Adjust based on your grid spacing
        int column = (int) (x / columnWidth);
        return Math.max(0, column);
    }
    
    /**
     * Calculate the row index based on y position
     */
    private int getRowIndex(double y) {
        // Simple calculation - can be refined based on your grid
        int rowHeight = 150; // Adjust based on your grid spacing
        int row = (int) (y / rowHeight);
        return Math.max(0, row);
    }
    
    /**
     * Select a table and update the UI
     */
    private void selectTable(TableStatus table) {
        this.selectedTable = table;
        selectedTableLabel.setText("Table " + table.getId() + " - Seats: " + 
                                  table.getSeatingCapacity() + " - " + table.getState());
        removeTableButton.setDisable(false);
    }
    
    /**
     * Add a new table to the layout
     */
    @FXML
    void onAddTableClicked(ActionEvent event) {
        // Find an empty cell
        int colIndex = 0;
        int rowIndex = 0;
        boolean foundEmptyCell = false;
        
        outerLoop:
        for (rowIndex = 0; rowIndex < 10; rowIndex++) {
            for (colIndex = 0; colIndex < 10; colIndex++) {
                boolean cellOccupied = false;
                
                for (TableStatus table : workingLayout.getAllTables()) {
                    if (table.getColumnIndex() == colIndex && table.getRowIndex() == rowIndex) {
                        cellOccupied = true;
                        break;
                    }
                }
                
                if (!cellOccupied) {
                    foundEmptyCell = true;
                    break outerLoop;
                }
            }
        }
        
        if (!foundEmptyCell) {
            // If no empty cell found, just add to the first position
            colIndex = 0;
            rowIndex = 0;
        }
        
        // Add a new table with the specified properties
        TableStatus newTable = workingLayout.addTable(
            new Point2D(0, 0),
            widthSpinner.getValue(),
            heightSpinner.getValue(),
            seatingSpinner.getValue(),
            tableStateComboBox.getValue(),
            colIndex,
            rowIndex
        );
        
        // Render the updated tables
        renderTables();
        
        // Select the new table
        selectTable(newTable);
    }
    
    /**
     * Remove the selected table
     */
    @FXML
    void onRemoveTableClicked(ActionEvent event) {
        if (selectedTable != null) {
            workingLayout.removeTable(selectedTable.getId());
            renderTables();
            
            // Clear selection
            selectedTable = null;
            selectedTableLabel.setText("No table selected");
            removeTableButton.setDisable(true);
        }
    }
    
    /**
     * Cancel editing and close the window
     */
    @FXML
    void onCancelClicked(ActionEvent event) {
        // Close the window without saving changes
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Save the layout and close the window
     */
    @FXML
    void onSaveClicked(ActionEvent event) {
        if (mainController != null) {
            // Update the layout in the main controller
            mainController.updateLayout(workingLayout);
        }
        
        // Close the window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Helper class to track mouse drag positions
     */
    private static class Delta {
        double x, y;
    }
}
