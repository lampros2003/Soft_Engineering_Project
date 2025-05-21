package com.tables;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TablesScreenController implements Initializable {

    @FXML 
    private GridPane tablesContainer;
    
    private final Color OCCUPIED_COLOR = Color.web("#F39C12"); // Orange for occupied tables
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green for available tables
    private final Color UNAVAILABLE_COLOR = Color.web("#AAAAAA"); // Gray for unavailable tables
    
    private final List<TableStatus> tables = new ArrayList<>();
    private final Map<Integer, Pane> tablePanes = new HashMap<>();
    private final Random random = new Random();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the table data
        initializeTableData();
        
        // Render tables from the data structure
        renderTables();
    }

    /**
     * Initialize the table data structure with information about each table
     */
    private void initializeTableData() {
        // Add sample table data with status
        // Table format: id, position, width, height, seating capacity, status, guests, time, server, column index, row inde
        // First floor section
        tables.add(new TableStatus(1, new Point2D(0, 0), 150, 100, 10, TableState.UNAVAILABLE, 0, null, "", 0, 0));
        tables.add(new TableStatus(2, new Point2D(0, 0), 120, 100, 8, TableState.UNAVAILABLE, 0, null, "", 1, 0));
        tables.add(new TableStatus(3, new Point2D(0, 0), 120, 100, 10, TableState.UNAVAILABLE, 0, null, "", 2, 0));
        tables.add(new TableStatus(4, new Point2D(0, 0), 150, 100, 12, TableState.UNAVAILABLE, 0, null, "", 3, 0));
        
        tables.add(new TableStatus(5, new Point2D(0, 0), 90, 100, 8, TableState.UNAVAILABLE, 0, null, "", 0, 1));
        tables.add(new TableStatus(6, new Point2D(0, 0), 70, 100, 4, TableState.OCCUPIED, 3, LocalTime.now().minusMinutes(45), "Sarah", 1, 1));
        tables.add(new TableStatus(7, new Point2D(0, 0), 100, 100, 8, TableState.UNAVAILABLE, 0, null, "", 2, 1));
        tables.add(new TableStatus(8, new Point2D(0, 0), 70, 100, 4, TableState.OCCUPIED, 2, LocalTime.now().minusMinutes(20), "John", 3, 1));
        
        tables.add(new TableStatus(9, new Point2D(0, 0), 120, 100, 8, TableState.AVAILABLE, 0, null, "", 0, 2));
        tables.add(new TableStatus(10, new Point2D(0, 0), 60, 100, 4, TableState.UNAVAILABLE, 0, null, "", 1, 2));
        tables.add(new TableStatus(11, new Point2D(0, 0), 100, 100, 6, TableState.OCCUPIED, 4, LocalTime.now().minusMinutes(15), "Mike", 2, 2));
        tables.add(new TableStatus(12, new Point2D(0, 0), 100, 100, 8, TableState.AVAILABLE, 0, null, "", 3, 2));
        
        // Adding more tables to demonstrate scrolling
        // VIP section
        tables.add(new TableStatus(13, new Point2D(0, 0), 180, 120, 14, TableState.AVAILABLE, 0, null, "", 0, 3));
        tables.add(new TableStatus(14, new Point2D(0, 0), 180, 120, 14, TableState.OCCUPIED, 10, LocalTime.now().minusMinutes(65), "Robert", 1, 3));
        tables.add(new TableStatus(15, new Point2D(0, 0), 180, 120, 14, TableState.AVAILABLE, 0, null, "", 2, 3));
        tables.add(new TableStatus(16, new Point2D(0, 0), 180, 120, 14, TableState.OCCUPIED, 12, LocalTime.now().minusMinutes(30), "Jennifer", 3, 3));
        
        // Patio tables
        tables.add(new TableStatus(17, new Point2D(0, 0), 80, 80, 4, TableState.UNAVAILABLE, 0, null, "", 0, 4));
        tables.add(new TableStatus(18, new Point2D(0, 0), 80, 80, 4, TableState.UNAVAILABLE, 0, null, "", 1, 4));
        tables.add(new TableStatus(19, new Point2D(0, 0), 80, 80, 4, TableState.AVAILABLE, 0, null, "", 2, 4));
        tables.add(new TableStatus(20, new Point2D(0, 0), 80, 80, 4, TableState.OCCUPIED, 2, LocalTime.now().minusMinutes(40), "Patricia", 3, 4));
        
        // Bar section
        tables.add(new TableStatus(21, new Point2D(0, 0), 60, 60, 2, TableState.OCCUPIED, 1, LocalTime.now().minusMinutes(10), "James", 0, 5));
        tables.add(new TableStatus(22, new Point2D(0, 0), 60, 60, 2, TableState.OCCUPIED, 2, LocalTime.now().minusMinutes(25), "Linda", 1, 5));
        tables.add(new TableStatus(23, new Point2D(0, 0), 60, 60, 2, TableState.AVAILABLE, 0, null, "", 2, 5));
        tables.add(new TableStatus(24, new Point2D(0, 0), 60, 60, 2, TableState.AVAILABLE, 0, null, "", 3, 5));
        
        // Private room
        tables.add(new TableStatus(25, new Point2D(0, 0), 200, 120, 16, TableState.OCCUPIED, 14, LocalTime.now().minusMinutes(75), "Birthday Party", 0, 6));
        tables.add(new TableStatus(26, new Point2D(0, 0), 200, 120, 16, TableState.AVAILABLE, 0, null, "", 2, 6));
    }

    /**
     * Render tables from the data structure
     */
    private void renderTables() {
        // Clear existing content in the grid
        tablesContainer.getChildren().clear();
        
        // Create and add tables to the grid
        for (TableStatus table : tables) {
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
            String occupiedTime = table.getOccupiedTime().format(formatter);
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
        System.out.println("Edit Layout clicked");
        // TODO: Open table layout editor screen

    }
    
    /**
     * Handle the Make Reservation button click
     */
    @FXML
    public void onMakeReservationClicked(ActionEvent event) {
        System.out.println("Make Reservation clicked");
        // TODO: Open reservation screen

    }

    @FXML
    public void onBackButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Get the Table object by ID
     */
    public TableStatus getTableById(int id) {
        return tables.stream()
                .filter(table -> table.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Enum representing the possible states of a table
     */
    public enum TableState {
        AVAILABLE,
        OCCUPIED,
        UNAVAILABLE
    }
}
