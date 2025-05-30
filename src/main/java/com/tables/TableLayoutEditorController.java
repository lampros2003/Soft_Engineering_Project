package com.tables;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.common.DBManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.application.Platform;

public class TableLayoutEditorController implements Initializable {

    @FXML private Canvas editorCanvas;
    @FXML private ScrollPane editorScrollPane;
    @FXML private AnchorPane editorCanvasContainer;

    @FXML private Spinner<Integer> seatingSpinner;
    @FXML private Spinner<Integer> widthSpinner;
    @FXML private Spinner<Integer> heightSpinner;
    @FXML private ComboBox<TablesScreenController.TableState> tableStateComboBox;
    @FXML private Label selectedTableLabel;
    @FXML private Button removeTableButton;


    private RestaurantLayout workingLayout;
    private final Map<Integer, TableBounds> tableBoundsMap = new HashMap<>();
    private TableStatus selectedTable = null;
    private DBManager dbManager;

    // Colors for different table states
    private final Color OCCUPIED_COLOR = Color.web("#F39C12"); // Orange
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green
    private final Color UNAVAILABLE_COLOR = Color.web("#AAAAAA"); // Gray

    // Constants for the canvas
    private static final double CANVAS_PADDING = 20;
    private static final double MIN_CANVAS_WIDTH = 800;
    private static final double MIN_CANVAS_HEIGHT = 600;
    private static final double ADDITIONAL_PADDING = 100; // Extra space beyond the last table

    // Variables for table dragging
    private TableStatus draggedTable = null;
    private double dragStartX;
    private double dragStartY;
    private double tableStartX;
    private double tableStartY;

    // Reference to the main controller to update when saving
    private TablesScreenController mainController;

    // Class to store table bounds for hit testing
    private record TableBounds(int tableId, double x, double y, double width, double height) {
        public boolean contains(double pointX, double pointY) {
            return pointX >= x && pointX <= x + width &&
                    pointY >= y && pointY <= y + height;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize database manager
        dbManager = new DBManager();

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

        // Set up listeners for canvas resizing
        setupCanvasSizeListener();

        // Add canvas event handlers for drag and drop
        editorCanvas.setOnMousePressed(this::onCanvasMousePressed);
        editorCanvas.setOnMouseDragged(this::onCanvasMouseDragged);
        editorCanvas.setOnMouseReleased(this::onCanvasMouseReleased);
        editorCanvas.setOnMouseClicked(this::onCanvasMouseClicked);
    }

    /**
     * Set up listeners for changes in scroll pane size
     */
    private void setupCanvasSizeListener() {
        // Listen for changes in the scroll pane's viewport size
        editorScrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            Platform.runLater(this::setupCanvas);
        });

        // Also add listener to scene changes to catch initial sizing
        editorScrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(this::setupCanvas);
            }
        });
    }

    /**
     * Set the main controller reference and initialize the working layout
     */
    public void setMainController(TablesScreenController controller) {
        this.mainController = controller;

        // Create a deep copy of the layout to work with (in memory only, no database operations)
        this.workingLayout = controller.getRestaurantLayout().createDeepCopy();

        // Set up the canvas and render the tables
        setupCanvas();
        renderTables();
    }

    /**
     * Set up the canvas with the appropriate size
     */
    private void setupCanvas() {
        // Calculate required canvas size based on table positions
        double maxX = CANVAS_PADDING;
        double maxY = CANVAS_PADDING;

        for (TableStatus table : workingLayout.getAllTables()) {
            // Get position - either from direct X/Y or from grid position
            double tableX;
            double tableY;

            Point2D position = table.getPosition();
            if (position != null && (position.getX() != 0 || position.getY() != 0)) {
                tableX = position.getX();
                tableY = position.getY();
            } else {
                tableX = table.getColumnIndex() * 150 + CANVAS_PADDING;
                tableY = table.getRowIndex() * 150 + CANVAS_PADDING;
            }

            double tableRight = tableX + table.getWidth();
            double tableBottom = tableY + table.getHeight();

            if (tableRight > maxX) maxX = tableRight;
            if (tableBottom > maxY) maxY = tableBottom;
        }

        // Add padding to ensure some extra space beyond the last table
        maxX += ADDITIONAL_PADDING;
        maxY += ADDITIONAL_PADDING;

        // Calculate minimum size based on viewport and content
        double viewportWidth = editorScrollPane.getViewportBounds().getWidth();
        double viewportHeight = editorScrollPane.getViewportBounds().getHeight();

        // The canvas should be at least as large as the viewport or the minimum size
        double canvasWidth = Math.max(Math.max(viewportWidth, MIN_CANVAS_WIDTH), maxX);
        double canvasHeight = Math.max(Math.max(viewportHeight, MIN_CANVAS_HEIGHT), maxY);

        // Set the size of the canvas
        editorCanvas.setWidth(canvasWidth);
        editorCanvas.setHeight(canvasHeight);

        // Also update the container's preferred size
        editorCanvasContainer.setPrefWidth(canvasWidth);
        editorCanvasContainer.setPrefHeight(canvasHeight);

        // Re-render tables since canvas size changed
        renderTables();
    }

    /**
     * Render tables on the canvas
     */
    private void renderTables() {
        // Clear the canvas and reset bounds map
        GraphicsContext gc = editorCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvas.getWidth(), editorCanvas.getHeight());
        tableBoundsMap.clear();

        // Draw a grid background for easy positioning reference
        drawGridBackground(gc);

        // Draw tables
        for (TableStatus table : workingLayout.getAllTables()) {
            drawTable(gc, table);
        }

        // If there's a selected table, highlight it
        if (selectedTable != null) {
            highlightSelectedTable(gc);
        }
    }

    /**
     * Draw a grid background on the canvas to help with positioning
     */
    private void drawGridBackground(GraphicsContext gc) {
        // Set grid line properties
        gc.setStroke(Color.web("#EEEEEE"));
        gc.setLineWidth(0.5);

        // Draw vertical grid lines
        for (int x = 0; x <= editorCanvas.getWidth(); x += 50) {
            gc.strokeLine(x, 0, x, editorCanvas.getHeight());
        }

        // Draw horizontal grid lines
        for (int y = 0; y <= editorCanvas.getHeight(); y += 50) {
            gc.strokeLine(0, y, editorCanvas.getWidth(), y);
        }
    }

    /**
     * Draw a table on the canvas
     */
    private void drawTable(GraphicsContext gc, TableStatus table) {
        // Get table position and dimensions
        double tableX;
        double tableY;

        // If the table has a position, use it, otherwise use grid coordinates
        Point2D position = table.getPosition();
        if (position != null && (position.getX() != 0 || position.getY() != 0)) {
            tableX = position.getX();
            tableY = position.getY();
        } else {
            tableX = table.getColumnIndex() * 150 + CANVAS_PADDING;
            tableY = table.getRowIndex() * 150 + CANVAS_PADDING;
        }

        double tableWidth = table.getWidth();
        double tableHeight = table.getHeight();

        // Set color based on table state
        Color tableColor = switch (table.getState()) {
            case AVAILABLE -> AVAILABLE_COLOR;
            case OCCUPIED -> OCCUPIED_COLOR;
            default -> UNAVAILABLE_COLOR;
        };

        // Draw table rectangle with rounded corners
        gc.setFill(tableColor);
        gc.fillRoundRect(tableX, tableY, tableWidth, tableHeight, 10, 10);

        // Draw table border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(tableX, tableY, tableWidth, tableHeight, 10, 10);

        // Add table ID
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(table.getId()), tableX + tableWidth/2, tableY + tableHeight/2 + 5);

        // Add seating capacity
        gc.setFont(new Font("Arial", 12));
        gc.fillText("Seats: " + table.getSeatingCapacity(), tableX + tableWidth/2, tableY + tableHeight/2 + 25);

        // Store table bounds for interaction
        tableBoundsMap.put(table.getId(), new TableBounds(table.getId(), tableX, tableY, tableWidth, tableHeight));
    }

    /**
     * Highlight the currently selected table
     */
    private void highlightSelectedTable(GraphicsContext gc) {
        TableBounds bounds = tableBoundsMap.get(selectedTable.getId());
        if (bounds != null) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(3);
            gc.strokeRoundRect(bounds.x - 5, bounds.y - 5, bounds.width + 10, bounds.height + 10, 15, 15);
        }
    }

    /**
     * Handle mouse press events on the canvas for table selection or drag start
     */
    private void onCanvasMousePressed(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        // Find if a table was clicked
        draggedTable = null;
        for (TableBounds bounds : tableBoundsMap.values()) {
            if (bounds.contains(mouseX, mouseY)) {
                // Get the table and prepare for dragging
                TableStatus table = getTableById(bounds.tableId());
                if (table != null) {
                    draggedTable = table;
                    dragStartX = mouseX;
                    dragStartY = mouseY;

                    // Get the initial table position
                    Point2D position = table.getPosition();
                    if (position != null && (position.getX() != 0 || position.getY() != 0)) {
                        tableStartX = position.getX();
                        tableStartY = position.getY();
                    } else {
                        tableStartX = table.getColumnIndex() * 150 + CANVAS_PADDING;
                        tableStartY = table.getRowIndex() * 150 + CANVAS_PADDING;
                    }
                    break;
                }
            }
        }
    }

    /**
     * Handle mouse drag events for moving tables
     */
    private void onCanvasMouseDragged(MouseEvent event) {
        if (draggedTable != null) {
            double mouseX = event.getX();
            double mouseY = event.getY();

            // Calculate the new table position
            double deltaX = mouseX - dragStartX;
            double deltaY = mouseY - dragStartY;

            double newX = tableStartX + deltaX;
            double newY = tableStartY + deltaY;

            // Ensure the table stays within the canvas bounds
            newX = Math.max(0, Math.min(newX, editorCanvas.getWidth() - draggedTable.getWidth()));
            newY = Math.max(0, Math.min(newY, editorCanvas.getHeight() - draggedTable.getHeight()));

            // Create a new version of the table with updated position
            TableStatus updatedTable = new TableStatus(
                draggedTable.getId(),
                new Point2D(newX, newY),
                draggedTable.getWidth(),
                draggedTable.getHeight(),
                draggedTable.getSeatingCapacity(),
                draggedTable.getState(),
                draggedTable.getGuestsCount(),
                draggedTable.getOccupiedTime(),
                draggedTable.getServerName(),
                0, // Column index not used with direct positioning
                0  // Row index is not used with direct positioning
            );

            // Replace the table in the working layout without saving to database
            workingLayout.replaceTableInMemory(updatedTable);

            // Update the dragged table reference
            draggedTable = updatedTable;

            // Re-render the tables
            renderTables();
        }
    }

    /**
     * Handle mouse release events to finalize table placement
     */
    private void onCanvasMouseReleased(MouseEvent event) {
        if (draggedTable != null) {
            // Set the selected table to the one that was just dragged
            selectedTable = draggedTable;
            selectedTableLabel.setText("Table " + selectedTable.getId() + " - Seats: " +
                                      selectedTable.getSeatingCapacity() + " - " + selectedTable.getState());
            removeTableButton.setDisable(false);

            draggedTable = null;
            renderTables();
        }
    }

    /**
     * Handle mouse clicks for selecting tables or placing new tables
     */
    private void onCanvasMouseClicked(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        // Check if this is a double click for placing a new table
        if (event.getClickCount() == 2) {
            // Place a new table at the clicked position
            addTableAt(mouseX, mouseY);
            return;
        }

        // Find if a table was clicked
        boolean tableClicked = false;
        for (TableBounds bounds : tableBoundsMap.values()) {
            if (bounds.contains(mouseX, mouseY)) {
                TableStatus table = getTableById(bounds.tableId());
                if (table != null) {
                    selectTable(table);
                    tableClicked = true;
                    break;
                }
            }
        }

        // If no table was clicked, clear the selection
        if (!tableClicked) {
            selectedTable = null;
            selectedTableLabel.setText("No table selected");
            removeTableButton.setDisable(true);
            renderTables();
        }
    }

    /**
     * Create a new table at the specified position
     */
    private void addTableAt(double x, double y) {
        // Create a new table at the specified position (in memory only)
        System.out.println("Adding new table at position (" + x + ", " + y + ")");
        
        TableStatus newTable = workingLayout.addTableInMemory(
            new Point2D(x, y),
            widthSpinner.getValue(),
            heightSpinner.getValue(),
            seatingSpinner.getValue(),
            tableStateComboBox.getValue(),
            0, // Column index not used with direct positioning
            0  // Row index is not used with direct positioning
        );
        
        System.out.println("Created new table with ID: " + newTable.getId());

        // Select the new table
        selectTable(newTable);

        // Re-render the tables
        renderTables();
    }

    /**
     * Select a table and update the UI
     */
    private void selectTable(TableStatus table) {
        this.selectedTable = table;
        selectedTableLabel.setText("Table " + table.getId() + " - Seats: " +
                                  table.getSeatingCapacity() + " - " + table.getState());
        removeTableButton.setDisable(false);

        // Update the spinner and combobox values to match the selected table
        seatingSpinner.getValueFactory().setValue(table.getSeatingCapacity());
        widthSpinner.getValueFactory().setValue((int) table.getWidth());
        heightSpinner.getValueFactory().setValue((int) table.getHeight());
        tableStateComboBox.setValue(table.getState());

        renderTables();
    }

    /**
     * Get a table by its ID
     */
    private TableStatus getTableById(int id) {
        return workingLayout.getTable(id);
    }

    /**
     * Add a new table to the layout
     */
    @FXML
    void onAddTableClicked(ActionEvent event) {
        // Find a suitable location for the new table
        double newX = CANVAS_PADDING;
        double newY = CANVAS_PADDING;

        // Try to avoid overlap with existing tables
        boolean foundPosition = false;
        for (int y = 0; y < 800; y += 150) {
            for (int x = 0; x < 800; x += 150) {
                if (isPositionFree(x, y, widthSpinner.getValue(), heightSpinner.getValue())) {
                    newX = x;
                    newY = y;
                    foundPosition = true;
                    break;
                }
            }
            if (foundPosition) break;
        }

        // Add a new table with the specified properties (in memory only)
        TableStatus newTable = workingLayout.addTableInMemory(
            new Point2D(newX, newY),
            widthSpinner.getValue(),
            heightSpinner.getValue(),
            seatingSpinner.getValue(),
            tableStateComboBox.getValue(),
            0, // Column index not used with direct positioning
            0  // Row index is not used with direct positioning
        );

        // Select the new table
        selectTable(newTable);

        // Render the updated tables
        renderTables();
    }

    /**
     * Check if a position is free of existing tables
     */
    private boolean isPositionFree(double x, double y, int width, int height) {
        for (TableBounds bounds : tableBoundsMap.values()) {
            // Check if the new table would overlap with an existing table
            if (x < bounds.x + bounds.width &&
                x + width > bounds.x &&
                y < bounds.y + bounds.height &&
                y + height > bounds.y) {
                return false; // Overlap detected
            }
        }
        return true; // No overlap found
    }

    /**
     * Remove the selected table
     */
    @FXML
    void onRemoveTableClicked(ActionEvent event) {
        if (selectedTable != null) {
            workingLayout.removeTableInMemory(selectedTable.getId());

            // Clear selection
            selectedTable = null;
            selectedTableLabel.setText("No table selected");
            removeTableButton.setDisable(true);

            // Re-render tables
            renderTables();
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
            System.out.println("Saving layout with " + workingLayout.getTableCount() + " tables");
            
            // Update the layout in the main controller
            // This will trigger saving to the database
            mainController.updateLayout(workingLayout);
        }

        // Close the window
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
