package com.tables;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import javafx.geometry.Point2D;

/**
 * RestaurantLayout class that stores and manages all tables in the restaurant.
 * This class acts as a data model for the restaurant layout.
 */
public class RestaurantLayout implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String layoutName;
    private String description;
    private final Map<Integer, TableStatus> tables;
    private int nextTableId;
    private int columns;
    private int rows;
    
    /**
     * Creates a new empty restaurant layout
     */
    public RestaurantLayout() {
        this("Default Layout", "Default restaurant layout");
    }
    
    /**
     * Creates a new restaurant layout with the given name and description
     */
    public RestaurantLayout(String layoutName, String description) {
        this.layoutName = layoutName;
        this.description = description;
        this.tables = new HashMap<>();
        this.nextTableId = 1;
        this.columns = 4; // Default columns
        this.rows = 7;    // Default rows
    }
    
    /**
     * Add a table to the layout
     */
    public TableStatus addTable(Point2D position, double width, double height, int seatingCapacity, 
                              TablesScreenController.TableState state, int columnIndex, int rowIndex) {
        TableStatus table = new TableStatus(
            nextTableId++,
            position,
            width,
            height,
            seatingCapacity,
            state,
            0,
            null,
            "",
            columnIndex,
            rowIndex
        );
        tables.put(table.getId(), table);
        
        // Update the grid dimensions if necessary
        if (columnIndex >= columns) columns = columnIndex + 1;
        if (rowIndex >= rows) rows = rowIndex + 1;
        
        return table;
    }
    
    /**
     * Get a table by its ID
     */
    public TableStatus getTable(int id) {
        return tables.get(id);
    }
    
    /**
     * Get all tables in the layout
     */
    public List<TableStatus> getAllTables() {
        return new ArrayList<>(tables.values());
    }
    
    /**
     * Get tables by their state
     */
    public List<TableStatus> getTablesByState(TablesScreenController.TableState state) {
        List<TableStatus> result = new ArrayList<>();
        for (TableStatus table : tables.values()) {
            if (table.getState() == state) {
                result.add(table);
            }
        }
        return result;
    }
    
    /**
     * Get available tables that can seat at least the specified number of guests
     */
    public List<TableStatus> getAvailableTablesForPartySize(int partySize) {
        List<TableStatus> result = new ArrayList<>();
        for (TableStatus table : tables.values()) {
            if (table.isAvailable() && table.getSeatingCapacity() >= partySize) {
                result.add(table);
            }
        }
        // Sort by capacity (ascending) so smallest suitable table comes first
        result.sort(Comparator.comparing(TableStatus::getSeatingCapacity));
        return result;
    }
    
    /**
     * Update table state, guests, and server when seating a party
     */
    public void seatParty(int tableId, int guestCount, String serverName) {
        TableStatus table = tables.get(tableId);
        if (table != null && table.isAvailable()) {
            table.setState(TablesScreenController.TableState.OCCUPIED);
            table.setGuestsCount(guestCount);
            table.setServerName(serverName);
            table.setOccupiedTime(LocalTime.now());
        }
    }
    
    /**
     * Mark a table as available (clear it)
     */
    public void clearTable(int tableId) {
        TableStatus table = tables.get(tableId);
        if (table != null && table.isOccupied()) {
            table.setState(TablesScreenController.TableState.AVAILABLE);
            table.setGuestsCount(0);
            table.setServerName("");
            table.setOccupiedTime(null);
        }
    }
    
    /**
     * Remove a table from the layout
     */
    public void removeTable(int tableId) {
        tables.remove(tableId);
    }
    
    /**
     * Move a table to a new position in the grid
     */
    public void moveTable(int tableId, int newColumnIndex, int newRowIndex) {
        TableStatus table = tables.get(tableId);
        if (table != null) {
            // Create a new table with updated position
            TableStatus updatedTable = new TableStatus(
                table.getId(),
                new Point2D(0, 0), // Actual positions will be calculated by the UI
                table.getWidth(),
                table.getHeight(),
                table.getSeatingCapacity(),
                table.getState(),
                table.getGuestsCount(),
                table.getOccupiedTime(),
                table.getServerName(),
                newColumnIndex,
                newRowIndex
            );
            
            // Replace the old table with the updated one
            tables.put(tableId, updatedTable);
            
            // Update the grid dimensions if necessary
            if (newColumnIndex >= columns) columns = newColumnIndex + 1;
            if (newRowIndex >= rows) rows = newRowIndex + 1;
        }
    }
    
    /**
     * Resize a table and update its seating capacity
     */
    public void resizeTable(int tableId, double width, double height, int seatingCapacity) {
        TableStatus table = tables.get(tableId);
        if (table != null) {
            // Create a new table with updated dimensions
            TableStatus updatedTable = new TableStatus(
                table.getId(),
                table.getPosition(),
                width,
                height,
                seatingCapacity,
                table.getState(),
                table.getGuestsCount(),
                table.getOccupiedTime(),
                table.getServerName(),
                table.getColumnIndex(),
                table.getRowIndex()
            );
            
            // Replace the old table with the updated one
            tables.put(tableId, updatedTable);
        }
    }
    
    /**
     * Save the restaurant layout to a file
     */
    public void saveToFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }
    
    /**
     * Load a restaurant layout from a file
     */
    public static RestaurantLayout loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (RestaurantLayout) ois.readObject();
        }
    }
    
    /**
     * Create a sample layout with default tables
     */
    public static RestaurantLayout createSampleLayout() {
        RestaurantLayout layout = new RestaurantLayout("Sample Layout", "A sample restaurant layout");
        
        // First floor section - row 0
        layout.addTable(new Point2D(0, 0), 150, 100, 10, TablesScreenController.TableState.UNAVAILABLE, 0, 0);
        layout.addTable(new Point2D(0, 0), 120, 100, 8, TablesScreenController.TableState.UNAVAILABLE, 1, 0);
        layout.addTable(new Point2D(0, 0), 120, 100, 10, TablesScreenController.TableState.UNAVAILABLE, 2, 0);
        layout.addTable(new Point2D(0, 0), 150, 100, 12, TablesScreenController.TableState.UNAVAILABLE, 3, 0);
        
        // Row 1
        layout.addTable(new Point2D(0, 0), 90, 100, 8, TablesScreenController.TableState.UNAVAILABLE, 0, 1);
        layout.addTable(new Point2D(0, 0), 70, 100, 4, TablesScreenController.TableState.OCCUPIED, 1, 1)
            .setGuestsCount(3);
        layout.addTable(new Point2D(0, 0), 100, 100, 8, TablesScreenController.TableState.UNAVAILABLE, 2, 1);
        layout.addTable(new Point2D(0, 0), 70, 100, 4, TablesScreenController.TableState.OCCUPIED, 3, 1)
            .setGuestsCount(2);
        
        // Row 2
        layout.addTable(new Point2D(0, 0), 120, 100, 8, TablesScreenController.TableState.AVAILABLE, 0, 2);
        layout.addTable(new Point2D(0, 0), 60, 100, 4, TablesScreenController.TableState.UNAVAILABLE, 1, 2);
        layout.addTable(new Point2D(0, 0), 100, 100, 6, TablesScreenController.TableState.OCCUPIED, 2, 2)
            .setGuestsCount(4);
        layout.addTable(new Point2D(0, 0), 100, 100, 8, TablesScreenController.TableState.AVAILABLE, 3, 2);
        
        // Add more tables similar to the existing sample data
        // VIP section - row 3
        layout.addTable(new Point2D(0, 0), 180, 120, 14, TablesScreenController.TableState.AVAILABLE, 0, 3);
        layout.addTable(new Point2D(0, 0), 180, 120, 14, TablesScreenController.TableState.OCCUPIED, 1, 3)
            .setGuestsCount(10);
        layout.addTable(new Point2D(0, 0), 180, 120, 14, TablesScreenController.TableState.AVAILABLE, 2, 3);
        layout.addTable(new Point2D(0, 0), 180, 120, 14, TablesScreenController.TableState.OCCUPIED, 3, 3)
            .setGuestsCount(12);
        
        // Continue adding more table sections
        
        return layout;
    }
    
    // Getters and setters
    
    public String getLayoutName() {
        return layoutName;
    }
    
    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getTableCount() {
        return tables.size();
    }
}
