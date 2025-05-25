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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class TablesScreenController implements Initializable {

    @FXML
    private Canvas tablesCanvas;

    @FXML
    private ScrollPane canvasScrollPane;

    @FXML
    private Pane canvasContainer;

    private final Color OCCUPIED_COLOR = Color.web("#F39C12"); // Orange for occupied tables
    private final Color AVAILABLE_COLOR = Color.web("#2ECC71"); // Green for available tables
    private final Color UNAVAILABLE_COLOR = Color.web("#AAAAAA"); // Gray for unavailable tables

    private RestaurantLayout restaurantLayout;
    private Map<Integer, TableBounds> tableBoundsMap = new HashMap<>();

    // Constants for the canvas
    private static final double CANVAS_PADDING = 20;
    private static final double CANVAS_WIDTH = 800;
    private static final double CANVAS_HEIGHT = 600;

    // Enum for table states
    public enum TableState {
        AVAILABLE,
        OCCUPIED,
        UNAVAILABLE
    }

    // Class to store table bounds for hit testing
        private record TableBounds(int tableId, double x, double y, double width, double height) {

        public boolean contains(double pointX, double pointY) {
                return pointX >= x && pointX <= x + width &&
                        pointY >= y && pointY <= y + height;
            }
        }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the restaurant layout
        restaurantLayout = RestaurantLayout.createSampleLayout();

        // Set up the canvas
        setupCanvas();

        // Render tables from the restaurant layout
        renderTables();

        // Add mouse click event handler to the canvas
        tablesCanvas.setOnMouseClicked(this::onCanvasClick);
    }

    /**
     * Set up the canvas with the right size
     */
    private void setupCanvas() {
        // Ensure the canvas is large enough for all tables
        double maxX = CANVAS_PADDING;
        double maxY = CANVAS_PADDING;

        for (TableStatus table : restaurantLayout.getAllTables()) {
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

        // Set canvas size with extra padding
        tablesCanvas.setWidth(Math.max(CANVAS_WIDTH, maxX + CANVAS_PADDING));
        tablesCanvas.setHeight(Math.max(CANVAS_HEIGHT, maxY + CANVAS_PADDING));
    }

    /**
     * Render tables on the canvas
     */
    private void renderTables() {
        // Clear the canvas and reset bounds map
        GraphicsContext gc = tablesCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, tablesCanvas.getWidth(), tablesCanvas.getHeight());
        tableBoundsMap.clear();

        // Draw a subtle grid background
        drawGridBackground(gc);

        // Create and add tables to the canvas
        for (TableStatus table : restaurantLayout.getAllTables()) {
            drawTable(gc, table);
        }
    }

    /**
     * Draw a subtle grid background on the canvas
     */
    private void drawGridBackground(GraphicsContext gc) {
        // Set grid line properties
        gc.setStroke(Color.web("#EEEEEE"));
        gc.setLineWidth(0.5);

        // Draw vertical grid lines
        for (int x = 0; x <= tablesCanvas.getWidth(); x += 50) {
            gc.strokeLine(x, 0, x, tablesCanvas.getHeight());
        }

        // Draw horizontal grid lines
        for (int y = 0; y <= tablesCanvas.getHeight(); y += 50) {
            gc.strokeLine(0, y, tablesCanvas.getWidth(), y);
        }
    }

    /**
     * Draw a table on the canvas
     */
    private void drawTable(GraphicsContext gc, TableStatus table) {
        // Convert grid positioning to actual coordinates
        double tableX;
        double tableY;

        // If the table has an actual position, use it, otherwise calculate from grid indices
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
        Color tableColor;
        switch (table.getState()) {
            case AVAILABLE:
                tableColor = AVAILABLE_COLOR;
                break;
            case OCCUPIED:
                tableColor = OCCUPIED_COLOR;
                break;
            case UNAVAILABLE:
            default:
                tableColor = UNAVAILABLE_COLOR;
                break;
        }

        // Draw table rectangle with rounded corners
        gc.setFill(tableColor);
        gc.fillRoundRect(tableX, tableY, tableWidth, tableHeight, 10, 10);

        // Draw table border
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(tableX, tableY, tableWidth, tableHeight, 10, 10);

        // Add table number
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(table.getId()), tableX + tableWidth/2, tableY + tableHeight/2 + 5);

        // Add chair markers for seating capacity visualization
        drawChairMarkers(gc, table, tableX, tableY, tableWidth, tableHeight);

        // If the table is occupied, add guest count indicator
        if (table.getState() == TableState.OCCUPIED) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("Arial", 12));
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("👥" + table.getGuestsCount(), tableX + 10, tableY + 20);
        }

        // Store table bounds for hit testing later
        tableBoundsMap.put(table.getId(), new TableBounds(table.getId(), tableX, tableY, tableWidth, tableHeight));
    }

    /**
     * Draw chair markers around a table
     */
    private void drawChairMarkers(GraphicsContext gc, TableStatus table, double tableX, double tableY,
                                 double tableWidth, double tableHeight) {
        int seating = table.getSeatingCapacity();
        Color markerColor;

        // Set chair marker color based on table state
        switch (table.getState()) {
            case AVAILABLE:
                markerColor = Color.web("#2ECC71").darker();
                break;
            case OCCUPIED:
                markerColor = Color.web("#F39C12").darker();
                break;
            default:
                markerColor = Color.web("#999999");
                break;
        }

        gc.setFill(markerColor);

        // Add top chairs (up to 5)
        int topChairs = Math.min(5, seating / 2);
        double topSpacing = tableWidth / (topChairs + 1);
        for (int i = 0; i < topChairs; i++) {
            double x = tableX + (i + 1) * topSpacing;
            double y = tableY - 10;
            gc.fillOval(x - 5, y - 5, 10, 10);
        }

        // Add bottom chairs (up to 5)
        int bottomChairs = Math.min(5, seating / 2);
        double bottomSpacing = tableWidth / (bottomChairs + 1);
        for (int i = 0; i < bottomChairs; i++) {
            double x = tableX + (i + 1) * bottomSpacing;
            double y = tableY + tableHeight + 10;
            gc.fillOval(x - 5, y - 5, 10, 10);
        }

        // Add left chairs if needed (for larger tables)
        if (seating > 10) {
            int leftChairs = Math.min(3, (seating - 10) / 2);
            double leftSpacing = tableHeight / (leftChairs + 1);
            for (int i = 0; i < leftChairs; i++) {
                double x = tableX - 10;
                double y = tableY + (i + 1) * leftSpacing;
                gc.fillOval(x - 5, y - 5, 10, 10);
            }
        }

        // Add right chairs if needed (for larger tables)
        if (seating > 10) {
            int rightChairs = Math.min(3, (seating - 10) / 2);
            double rightSpacing = tableHeight / (rightChairs + 1);
            for (int i = 0; i < rightChairs; i++) {
                double x = tableX + tableWidth + 10;
                double y = tableY + (i + 1) * rightSpacing;
                gc.fillOval(x - 5, y - 5, 10, 10);
            }
        }
    }

    /**
     * Handle click on the canvas to detect table clicks
     */
    @FXML
    public void onCanvasClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        // Find the table that was clicked
        Optional<TableBounds> clickedTable = tableBoundsMap.values().stream()
            .filter(bounds -> bounds.contains(mouseX, mouseY))
            .findFirst();

        if (clickedTable.isPresent()) {
            TableStatus table = getTableById(clickedTable.get().tableId());

            if (table != null) {
                System.out.println("Table details: " + table.toString());
                // Could show a dialog with table details here
            }
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
        setupCanvas();
        renderTables();
    }
}
