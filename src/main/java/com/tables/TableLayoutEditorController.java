package com.tables;

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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.util.Pair;
import java.util.Optional;

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

    // Colors for different table states
    private final Color OCCUPIED_COLOR = Color.web("#F39C12"); // Orange
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green
    private final Color UNAVAILABLE_COLOR = Color.web("#AAAAAA"); // Gray

    // Constants for the canvas
    private static final double CANVAS_PADDING = 20;
    private static final double MIN_CANVAS_WIDTH = 800;
    private static final double MIN_CANVAS_HEIGHT = 600;
    private static final double ADDITIONAL_PADDING = 100; // Extra space beyond the last table
    private static final double GRID_CELL_SIZE = 150.0; // Added constant

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
        editorScrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> Platform.runLater(this::setupCanvas));

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
        this.workingLayout = controller.getRestaurantLayout().createDeepCopy();
        Platform.runLater(this::setupCanvas); // Ensure canvas is set up after scene is available
    }

    /**
     * Helper method to get the visual coordinates of a table on the canvas.
     * It uses the table's direct position if available and valid (not 0,0),
     * otherwise calculates based on grid indices.
     */
    private Point2D getTableVisualCoordinates(TableStatus table) {
        Point2D position = table.getPosition();
        if (position != null && (position.getX() != 0 || position.getY() != 0)) {
            return position;
        } else {
            double x = table.getColumnIndex() * GRID_CELL_SIZE + CANVAS_PADDING;
            double y = table.getRowIndex() * GRID_CELL_SIZE + CANVAS_PADDING;
            return new Point2D(x, y);
        }
    }

    /**
     * Set up the canvas with the appropriate size
     */
    private void setupCanvas() {
        if (workingLayout == null) return; // Guard against null layout

        double maxX = CANVAS_PADDING;
        double maxY = CANVAS_PADDING;

        for (TableStatus table : workingLayout.getAllTables()) {
            Point2D visualPos = getTableVisualCoordinates(table);
            double tableX = visualPos.getX();
            double tableY = visualPos.getY();

            double tableRight = tableX + table.getWidth();
            double tableBottom = tableY + table.getHeight();

            if (tableRight > maxX) maxX = tableRight;
            if (tableBottom > maxY) maxY = tableBottom;
        }

        maxX += ADDITIONAL_PADDING;
        maxY += ADDITIONAL_PADDING;

        double viewportWidth = editorScrollPane.getViewportBounds().getWidth();
        double viewportHeight = editorScrollPane.getViewportBounds().getHeight();

        double canvasWidth = Math.max(Math.max(viewportWidth, MIN_CANVAS_WIDTH), maxX);
        double canvasHeight = Math.max(Math.max(viewportHeight, MIN_CANVAS_HEIGHT), maxY);

        editorCanvas.setWidth(canvasWidth);
        editorCanvas.setHeight(canvasHeight);
        editorCanvasContainer.setPrefWidth(canvasWidth);
        editorCanvasContainer.setPrefHeight(canvasHeight);

        renderTables();
    }

    /**
     * Render tables on the canvas
     */
    private void renderTables() {
        if (workingLayout == null) return; // Guard against null layout

        GraphicsContext gc = editorCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, editorCanvas.getWidth(), editorCanvas.getHeight());
        tableBoundsMap.clear();
        drawGridBackground(gc);

        for (TableStatus table : workingLayout.getAllTables()) {
            drawTable(gc, table);
        }

        if (selectedTable != null) {
            highlightSelectedTable(gc);
        }
    }

    /**
     * Draw a grid background on the canvas to help with positioning
     */
    private void drawGridBackground(GraphicsContext gc) {
        gc.setStroke(Color.web("#EEEEEE"));
        gc.setLineWidth(0.5);
        for (double x = 0; x <= editorCanvas.getWidth(); x += 50) { // Grid lines every 50px
            gc.strokeLine(x, 0, x, editorCanvas.getHeight());
        }
        for (double y = 0; y <= editorCanvas.getHeight(); y += 50) {
            gc.strokeLine(0, y, editorCanvas.getWidth(), y);
        }
    }

    /**
     * Draw a table on the canvas
     */
    private void drawTable(GraphicsContext gc, TableStatus table) {
        Point2D visualPos = getTableVisualCoordinates(table);
        double tableX = visualPos.getX();
        double tableY = visualPos.getY();
        double tableWidth = table.getWidth();
        double tableHeight = table.getHeight();

        Color tableColor = switch (table.getState()) {
            case AVAILABLE -> AVAILABLE_COLOR;
            case OCCUPIED -> OCCUPIED_COLOR;
            default -> UNAVAILABLE_COLOR;
        };

        gc.setFill(tableColor);
        gc.fillRoundRect(tableX, tableY, tableWidth, tableHeight, 10, 10);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(tableX, tableY, tableWidth, tableHeight, 10, 10);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(table.getId()), tableX + tableWidth / 2, tableY + tableHeight / 2 + 5);
        gc.setFont(new Font("Arial", 12));
        gc.fillText("Seats: " + table.getSeatingCapacity(), tableX + tableWidth / 2, tableY + tableHeight / 2 + 25);

        tableBoundsMap.put(table.getId(), new TableBounds(table.getId(), tableX, tableY, tableWidth, tableHeight));
    }

    /**
     * Highlight the currently selected table
     */
    private void highlightSelectedTable(GraphicsContext gc) {
        if (selectedTable == null) return;
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
        draggedTable = null;
        for (TableBounds bounds : tableBoundsMap.values()) {
            if (bounds.contains(mouseX, mouseY)) {
                TableStatus table = getTableById(bounds.tableId());
                if (table != null) {
                    draggedTable = table;
                    dragStartX = mouseX;
                    dragStartY = mouseY;
                    Point2D initialVisualPos = getTableVisualCoordinates(table);
                    tableStartX = initialVisualPos.getX();
                    tableStartY = initialVisualPos.getY();
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
            double deltaX = mouseX - dragStartX;
            double deltaY = mouseY - dragStartY;
            double newX = tableStartX + deltaX;
            double newY = tableStartY + deltaY;

            // Ensure the table stays within the canvas bounds (0,0 to canvas.width/height - table.width/height)
            newX = Math.max(CANVAS_PADDING, Math.min(newX, editorCanvas.getWidth() - draggedTable.getWidth() - CANVAS_PADDING));
            newY = Math.max(CANVAS_PADDING, Math.min(newY, editorCanvas.getHeight() - draggedTable.getHeight() - CANVAS_PADDING));

            // Check for overlaps before moving
            if (!isPositionFree(newX, newY, draggedTable.getWidth(), draggedTable.getHeight(), draggedTable)) {
                return; // Don't move if it overlaps
            }

            TableStatus updatedTable = new TableStatus(
                draggedTable.getId(), new Point2D(newX, newY), draggedTable.getWidth(), draggedTable.getHeight(),
                draggedTable.getSeatingCapacity(), draggedTable.getState(), draggedTable.getGuestsCount(),
                draggedTable.getOccupiedTime(), draggedTable.getServerName(),
                0, 0 // Grid indices are not primary for direct positioning
            );
            workingLayout.replaceTableInMemory(updatedTable);
            draggedTable = updatedTable; // Update reference to the dragged table
            renderTables();
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Handle mouse release events to finalize table placement
     */
    private void onCanvasMouseReleased(MouseEvent event) {
        if (draggedTable != null) {
            // Check for overlaps one last time before finalizing
            if (!isPositionFree(draggedTable.getPosition().getX(), draggedTable.getPosition().getY(), draggedTable.getWidth(), draggedTable.getHeight(), draggedTable)) {
                showErrorAlert("Placement Conflict", "The table cannot be placed here as it overlaps with another table. Please try a different location.");
                TableStatus revertedTable = new TableStatus(
                    draggedTable.getId(), new Point2D(tableStartX, tableStartY), draggedTable.getWidth(), draggedTable.getHeight(),
                    draggedTable.getSeatingCapacity(), draggedTable.getState(), draggedTable.getGuestsCount(),
                    draggedTable.getOccupiedTime(), draggedTable.getServerName(),
                    0, 0
                );
                workingLayout.replaceTableInMemory(revertedTable);
                draggedTable = revertedTable; // update draggedTable to the reverted one
            }

            TableStatus tableToSelect = draggedTable;
            draggedTable = null;
            selectTable(tableToSelect); // This will call renderTables
        }
    }

    /**
     * Handle mouse clicks for selecting tables or placing new tables
     */
    private void onCanvasMouseClicked(MouseEvent event) {
        if (draggedTable != null) return; // Don't process click if a drag was just completed

        double mouseX = event.getX();
        double mouseY = event.getY();

        // Check if we clicked on an existing table
        TableStatus clickedTable = null;
        for (TableBounds bounds : tableBoundsMap.values()) {
            if (bounds.contains(mouseX, mouseY)) {
                clickedTable = getTableById(bounds.tableId());
                break;
            }
        }

        // Handle double-click
        if (event.getClickCount() == 2) {
            if (clickedTable != null) {
                // Double-clicked on an existing table - edit it
                showTableEditDialog(clickedTable);
                return;
            } else {
                // Double-clicked on empty space - add new table
                addTableAt(mouseX, mouseY);
                return;
            }
        }

        // Handle single-click (selection)
        if (clickedTable != null) {
            selectTable(clickedTable);
        } else {
            clearTableSelection();
        }
    }

    /**
     * Create a new table at the specified position (on double-click)
     */
    private void addTableAt(double x, double y) {
        double newTableWidth = widthSpinner.getValue();
        double newTableHeight = heightSpinner.getValue();

        // Adjust x, y to be the top-left corner for the new table, centered on the click
        double adjustedX = x - newTableWidth / 2;
        double adjustedY = y - newTableHeight / 2;

        // Ensure it's within canvas padding
        adjustedX = Math.max(CANVAS_PADDING, Math.min(adjustedX, editorCanvas.getWidth() - newTableWidth - CANVAS_PADDING));
        adjustedY = Math.max(CANVAS_PADDING, Math.min(adjustedY, editorCanvas.getHeight() - newTableHeight - CANVAS_PADDING));

        if (!isPositionFree(adjustedX, adjustedY, newTableWidth, newTableHeight, null)) {
            showErrorAlert("Cannot Add Table", "The selected position overlaps with an existing table.");
            return;
        }

        TableStatus newTable = workingLayout.addTableInMemory(
            new Point2D(adjustedX, adjustedY), newTableWidth, newTableHeight,
            seatingSpinner.getValue(), tableStateComboBox.getValue(),
            0, 0 // Grid indices not primary
        );
        selectTable(newTable); // This calls renderTables
    }

    /**
     * Select a table and update the UI
     */
    private void selectTable(TableStatus table) {
        this.selectedTable = table;
        if (table != null) {
            selectedTableLabel.setText("Table " + table.getId() + " - Seats: " +
                                      table.getSeatingCapacity() + " - " + table.getState());
            removeTableButton.setDisable(false);
            seatingSpinner.getValueFactory().setValue(table.getSeatingCapacity());
            widthSpinner.getValueFactory().setValue((int) Math.round(table.getWidth()));
            heightSpinner.getValueFactory().setValue((int) Math.round(table.getHeight()));
            tableStateComboBox.setValue(table.getState());
        } else {
            clearTableSelection(); // Handles UI update for no selection
        }
        renderTables();
    }

    /**
     * Clears the current table selection and updates the UI accordingly.
     */
    private void clearTableSelection() {
        selectedTable = null;
        selectedTableLabel.setText("No table selected");
        removeTableButton.setDisable(true);
        renderTables();
    }

    /**
     * Get a table by its ID
     */
    private TableStatus getTableById(int id) {
        if (workingLayout == null) return null;
        return workingLayout.getTable(id);
    }

    /**
     * Add a new table to the layout (via button)
     */
    @FXML
    void onAddTableClicked(ActionEvent event) {
        double newTableWidth = widthSpinner.getValue();
        double newTableHeight = heightSpinner.getValue();
        double newX = CANVAS_PADDING;
        double newY = CANVAS_PADDING;
        boolean foundPosition = false;

        // Try to find a free grid spot
        for (double ySearch = CANVAS_PADDING; ySearch < editorCanvas.getHeight() - newTableHeight; ySearch += GRID_CELL_SIZE / 2) {
            for (double xSearch = CANVAS_PADDING; xSearch < editorCanvas.getWidth() - newTableWidth; xSearch += GRID_CELL_SIZE / 2) {
                if (isPositionFree(xSearch, ySearch, newTableWidth, newTableHeight, null)) {
                    newX = xSearch;
                    newY = ySearch;
                    foundPosition = true;
                    break;
                }
            }
            if (foundPosition) break;
        }

        if (!foundPosition) {
            showErrorAlert("Cannot Add Table", "No free space found to add a new table automatically. Try double-clicking on a free spot.");
            return;
        }

        TableStatus newTable = workingLayout.addTableInMemory(
            new Point2D(newX, newY), newTableWidth, newTableHeight,
            seatingSpinner.getValue(), tableStateComboBox.getValue(),
            0, 0 // Grid indices not primary
        );
        selectTable(newTable);
    }

    /**
     * Check if a position is free of existing tables, optionally ignoring one table.
     * @param tableToIgnore The table to ignore during checks (e.g., the one being dragged). Can be null.
     */
    private boolean isPositionFree(double x, double y, double width, double height, TableStatus tableToIgnore) {
        if (workingLayout == null) return true; // If no layout, position is free

        for (TableStatus existingTable : workingLayout.getAllTables()) {
            if (tableToIgnore != null && existingTable.getId() == tableToIgnore.getId()) {
                continue; // Skip checking against the table itself
            }

            Point2D existingPos = getTableVisualCoordinates(existingTable);
            double existingX = existingPos.getX();
            double existingY = existingPos.getY();
            double existingWidth = existingTable.getWidth();
            double existingHeight = existingTable.getHeight();

            // Standard Axis-Aligned Bounding Box (AABB) collision detection
            if (x < existingX + existingWidth &&
                x + width > existingX &&
                y < existingY + existingHeight &&
                y + height > existingY) {
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
        if (selectedTable != null && workingLayout != null) {
            workingLayout.removeTableInMemory(selectedTable.getId());
            clearTableSelection(); // This calls renderTables
        }
    }

    /**
     * Cancel editing and close the window
     */
    @FXML
    void onCancelClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Save the layout and close the window
     */
    @FXML
    void onSaveClicked(ActionEvent event) {
        if (mainController != null && workingLayout != null) {
            mainController.updateLayout(workingLayout);
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Show a dialog to edit table properties
     */
    private void showTableEditDialog(TableStatus table) {
        if (table == null || workingLayout == null) return;

        // Create the custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Table " + table.getId());
        dialog.setHeaderText("Edit table properties");

        // Set the button types (OK and Cancel)
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create a grid for the form layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // Create form controls with the table's current values
        Spinner<Integer> editSeatingSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, table.getSeatingCapacity()));
        Spinner<Integer> editWidthSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(60, 200, (int) Math.round(table.getWidth()), 10));
        Spinner<Integer> editHeightSpinner = new Spinner<>(new SpinnerValueFactory.IntegerSpinnerValueFactory(60, 180, (int) Math.round(table.getHeight()), 10));

        ComboBox<TablesScreenController.TableState> editStateComboBox = new ComboBox<>(FXCollections.observableArrayList(
            TablesScreenController.TableState.AVAILABLE,
            TablesScreenController.TableState.OCCUPIED,
            TablesScreenController.TableState.UNAVAILABLE
        ));
        editStateComboBox.setValue(table.getState());

        // Add controls to the grid
        grid.add(new Label("Seating Capacity:"), 0, 0);
        grid.add(editSeatingSpinner, 1, 0);
        grid.add(new Label("Width:"), 0, 1);
        grid.add(editWidthSpinner, 1, 1);
        grid.add(new Label("Height:"), 0, 2);
        grid.add(editHeightSpinner, 1, 2);
        grid.add(new Label("State:"), 0, 3);
        grid.add(editStateComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the seating field by default
        Platform.runLater(editSeatingSpinner::requestFocus);

        // Show the dialog and process the result
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Point2D position = table.getPosition();

            // Create updated table with new values
            TableStatus updatedTable = new TableStatus(
                table.getId(),
                position,
                editWidthSpinner.getValue(),
                editHeightSpinner.getValue(),
                editSeatingSpinner.getValue(),
                editStateComboBox.getValue(),
                table.getGuestsCount(),
                table.getOccupiedTime(),
                table.getServerName(),
                table.getColumnIndex(),
                table.getRowIndex()
            );

            // Validate changes (e.g., check for overlaps with new size)
            if (table.getWidth() != updatedTable.getWidth() || table.getHeight() != updatedTable.getHeight()) {
                // Table size changed, check for overlaps
                if (!isPositionFree(position.getX(), position.getY(),
                        updatedTable.getWidth(), updatedTable.getHeight(), table)) {
                    showErrorAlert("Size Change Conflict",
                        "The new table size would cause an overlap with another table. Changes not applied.");
                    return;
                }
            }

            // Apply the changes
            workingLayout.replaceTableInMemory(updatedTable);
            selectTable(updatedTable); // This will update UI and canvas
        }
    }
}
