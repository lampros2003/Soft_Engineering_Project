package com.tables;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class TablesScreenController implements Initializable {

    @FXML 
    private GridPane tablesContainer;
    
    private final Color OCCUPIED_COLOR = Color.web("#F39C12"); // Orange for occupied tables
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green for available tables
    private final Color UNAVAILABLE_COLOR = Color.web("#AAAAAA"); // Gray for unavailable tables
    
    private RestaurantLayout restaurantLayout;
    private final Map<Integer, Pane> tablePanes = new HashMap<>();
    
    // Enum for table states
    public enum TableState {
        AVAILABLE,
        OCCUPIED,
        UNAVAILABLE
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the restaurant layout
        restaurantLayout = RestaurantLayout.createSampleLayout();
        
        // Render tables from the restaurant layout
        renderTables();
    }

    /**
     * Render tables from the restaurant layout
     */
    private void renderTables() {
        // Clear existing content in the grid
        tablesContainer.getChildren().clear();
        tablePanes.clear();
        
        // Create and add tables to the grid
        for (TableStatus table : restaurantLayout.getAllTables()) {
            Pane tablePane = createTablePane(table);
            tablePanes.put(table.getId(), tablePane);
            
            // Add table to the grid at the specified position
            tablesContainer.add(tablePane, table.getColumnIndex(), table.getRowIndex());
        }
    }
    
    /**
     * Create a table pane based on the table data
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
        
        // Add chair markers based on seating capacity
        addChairMarkers(tablePane, table);
        
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
        
        // Add tooltip showing detailed status information for occupied tables
        if (table.getState() == TableState.OCCUPIED) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String occupiedTime = table.getOccupiedTime() != null 
                ? table.getOccupiedTime().format(formatter) 
                : "unknown";
                
            String tooltipText = String.format(
                "Table %d\nGuests: %d\nServer: %s\nOccupied since: %s",
                table.getId(),
                table.getGuestsCount(),
                table.getServerName(),
                occupiedTime
            );
            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(tablePane, tooltip);
            
            // Add status indicators to the table
            // Add small icon or text to indicate occupied status
            Text statusIcon = new Text("👥" + table.getGuestsCount());
            statusIcon.setFill(Color.WHITE);
            statusIcon.setStyle("-fx-font-weight: bold;");
            statusIcon.setTranslateX(10);
            statusIcon.setTranslateY(15);
            tablePane.getChildren().add(statusIcon);
        }
        
        // Add mouse click event handler
        tablePane.setOnMouseClicked(this::onTableClick);
        
        stackPane.getChildren().add(tablePane);
        return stackPane;
    }
    
    /**
     * Add chair markers to a table based on its seating capacity
     */
    private void addChairMarkers(Pane tablePane, TableStatus table) {
        int seating = table.getSeatingCapacity();
        double width = table.getWidth();
        double height = table.getHeight();
        String chairStyle = "chair-marker";
        
        switch (table.getState()) {
            case AVAILABLE:
                chairStyle = "chair-marker-available";
                break;
            case OCCUPIED:
                chairStyle = "chair-marker-occupied";
                break;
            default:
                chairStyle = "chair-marker";
                break;
        }
        
        // Add top chairs (up to 5)
        int topChairs = Math.min(5, seating / 2);
        double topSpacing = width / (topChairs + 1);
        for (int i = 0; i < topChairs; i++) {
            Pane chair = new Pane();
            chair.getStyleClass().add(chairStyle);
            chair.setLayoutX((i + 1) * topSpacing - 5);
            chair.setLayoutY(-15);
            tablePane.getChildren().add(chair);
        }
        
        // Add bottom chairs (up to 5)
        int bottomChairs = Math.min(5, seating / 2);
        double bottomSpacing = width / (bottomChairs + 1);
        for (int i = 0; i < bottomChairs; i++) {
            Pane chair = new Pane();
            chair.getStyleClass().add(chairStyle);
            chair.setLayoutX((i + 1) * bottomSpacing - 5);
            chair.setLayoutY(height + 5);
            tablePane.getChildren().add(chair);
        }
        
        // Add left chairs if needed (for larger tables)
        if (seating > 10) {
            int leftChairs = Math.min(3, (seating - 10) / 2);
            double leftSpacing = height / (leftChairs + 1);
            for (int i = 0; i < leftChairs; i++) {
                Pane chair = new Pane();
                chair.getStyleClass().add(chairStyle);
                chair.setLayoutX(-15);
                chair.setLayoutY((i + 1) * leftSpacing - 5);
                tablePane.getChildren().add(chair);
            }
        }
        
        // Add right chairs if needed (for larger tables)
        if (seating > 10) {
            int rightChairs = Math.min(3, (seating - 10) / 2);
            double rightSpacing = height / (rightChairs + 1);
            for (int i = 0; i < rightChairs; i++) {
                Pane chair = new Pane();
                chair.getStyleClass().add(chairStyle);
                chair.setLayoutX(width + 5);
                chair.setLayoutY((i + 1) * rightSpacing - 5);
                tablePane.getChildren().add(chair);
            }
        }
    }

    /**
     * Handle table click to show detailed information
     */
    @FXML
    public void onTableClick(MouseEvent event) {
        Pane tablePane = (Pane) event.getSource();
        String tableId = tablePane.getId().replace("table", "");
        TableStatus table = getTableById(Integer.parseInt(tableId));
        
        if (table != null) {
            System.out.println("Table details: " + table.toString());
            // Could show a dialog with table details here
        }
    }

    /**
     * Handle the Edit Layout button click
     */
    @FXML
    public void onEditLayoutClicked(ActionEvent event) {
        try {
            // Load the table layout editor FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tables/TableLayoutEditor.fxml"));
            Parent root = loader.load();
            
            // Get the controller and set the main controller reference
            TableLayoutEditorController editorController = loader.getController();
            editorController.setMainController(this);
            
            // Create a new stage for the editor
            Stage editorStage = new Stage();
            editorStage.setTitle("Edit Table Layout");
            editorStage.setScene(new Scene(root));
            
            // Set modality to block input to other windows
            editorStage.initModality(Modality.WINDOW_MODAL);
            editorStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            
            // Show the editor
            editorStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Handle the Make Reservation button click
     */
    @FXML
    public void onMakeReservationClicked(ActionEvent event) {
        // TODO: Implement reservation functionality
        System.out.println("Make Reservation clicked - to be implemented");
    }
    
    /**
     * Handle the Back button click
     */
    @FXML
    public void onBackButtonClicked(ActionEvent event) {
        // Return to previous screen
        // For now, just print a message
        System.out.println("Back button clicked - would return to previous screen");
    }
    
    /**
     * Get a table by its ID
     */
    private TableStatus getTableById(int id) {
        return restaurantLayout.getTable(id);
    }
    
    /**
     * Get the restaurant layout
     * This is used by the editor controller
     */
    public RestaurantLayout getRestaurantLayout() {
        return restaurantLayout;
    }
    
    /**
     * Update the restaurant layout and refresh the UI
     * This is called by the editor controller when the user saves changes
     */
    public void updateLayout(RestaurantLayout newLayout) {
        this.restaurantLayout = newLayout;
        renderTables();
    }
}
