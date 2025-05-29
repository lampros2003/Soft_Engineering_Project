package com.tables;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import javafx.geometry.Point2D;
import com.common.DBManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    private final DBManager dbManager;
    private boolean loaded = false;

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
        this.dbManager = new DBManager();

        // Initialize the database table structure if needed
        dbManager.initializeTableStructure();

        // Set default grid dimensions
        this.columns = 4;
        this.rows = 7;
    }

    /**
     * Load tables from the database if not already loaded
     */
    public void loadTablesFromDatabase() {
        if (loaded) {
            return; // Prevent loading multiple times
        }

        tables.clear(); // Clear any existing tables before loading
        List<TableStatus> loadedTables = dbManager.getTables();
        for (TableStatus table : loadedTables) {
            tables.put(table.getId(), table);
        }
        
        // Set the next available ID based on loaded tables
        this.nextTableId = dbManager.getNextTableId();

        // Update grid dimensions based on loaded tables
        updateGridDimensions();
        
        loaded = true;
        System.out.println("Loaded " + loadedTables.size() + " tables from database");
    }

    /**
     * Update grid dimensions based on table positions
     */
    private void updateGridDimensions() {
        for (TableStatus table : tables.values()) {
            if (table.getColumnIndex() >= columns) columns = table.getColumnIndex() + 1;
            if (table.getRowIndex() >= rows) rows = table.getRowIndex() + 1;
        }
    }

    /**
     * Add a table to the layout (in memory only)
     */
    public TableStatus addTableInMemory(Point2D position, double width, double height, int seatingCapacity,
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
     * Add a table to the layout and save to database
     */
    public TableStatus addTable(Point2D position, double width, double height, int seatingCapacity,
                                TablesScreenController.TableState state, int columnIndex, int rowIndex) {
        TableStatus table = addTableInMemory(position, width, height, seatingCapacity, state, columnIndex, rowIndex);
        
        // Save the new table to the database
        dbManager.saveTable(table);

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
            dbManager.saveTable(table); // Update table in database
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
            dbManager.saveTable(table); // Update table in database
        }
    }

    /**
     * Remove a table from the layout and database
     */
    public void removeTable(int tableId) {
        tables.remove(tableId);
        dbManager.deleteTable(tableId); // Remove table from database
    }
    
    /**
     * Remove a table from the layout (in memory only)
     */
    public void removeTableInMemory(int tableId) {
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

            // Save the updated table to the database
            dbManager.saveTable(updatedTable);
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

            // Save the updated table to the database
            dbManager.saveTable(updatedTable);
        }
    }

    /**
     * Replace a table in the layout with an updated version (in memory only)
     */
    public void replaceTableInMemory(TableStatus updatedTable) {
        if (updatedTable != null) {
            tables.put(updatedTable.getId(), updatedTable);

            // Update grid dimensions if necessary based on column/row indices
            int columnIndex = updatedTable.getColumnIndex();
            int rowIndex = updatedTable.getRowIndex();
            if (columnIndex >= columns) columns = columnIndex + 1;
            if (rowIndex >= rows) rows = rowIndex + 1;
        }
    }

    /**
     * Replace a table in the layout with an updated version and save to database
     */
    public void replaceTable(TableStatus updatedTable) {
        replaceTableInMemory(updatedTable);
        
        // Save the updated table to the database
        if (updatedTable != null) {
            dbManager.saveTable(updatedTable);
        }
    }

    /**
     * Save all tables to the database (useful after bulk updates)
     */
    public void saveAllTablesToDatabase() {
        // First get existing table IDs from the database for comparison
        List<Integer> existingTableIds = new ArrayList<>();
        List<TableStatus> currentTables = dbManager.getTables();
        for (TableStatus table : currentTables) {
            existingTableIds.add(table.getId());
        }
        
        // Keep track of saved tables to identify deleted ones
        Set<Integer> savedTableIds = new HashSet<>();
        
        // Save all tables in the layout
        for (TableStatus table : tables.values()) {
            dbManager.saveTable(table);
            savedTableIds.add(table.getId());
        }
        
        // Delete any tables that exist in the database but not in the layout
        for (Integer existingId : existingTableIds) {
            if (!savedTableIds.contains(existingId)) {
                dbManager.deleteTable(existingId);
                System.out.println("Deleted table from database: Table " + existingId);
            }
        }
        
        System.out.println("Saved " + tables.size() + " tables to database");
    }

    /**
     * Create a deep copy of this layout (in memory only, no database operations)
     */
    public RestaurantLayout createDeepCopy() {
        RestaurantLayout copy = new RestaurantLayout(this.layoutName, this.description);
        copy.nextTableId = this.nextTableId;
        copy.columns = this.columns;
        copy.rows = this.rows;
        copy.loaded = true;  // Mark as loaded to prevent additional DB loads
        
        // Copy all tables without saving to database
        for (TableStatus table : this.tables.values()) {
            TableStatus tableCopy = new TableStatus(
                table.getId(),
                table.getPosition(),
                table.getWidth(),
                table.getHeight(),
                table.getSeatingCapacity(),
                table.getState(),
                table.getGuestsCount(),
                table.getOccupiedTime(),
                table.getServerName(),
                table.getColumnIndex(),
                table.getRowIndex()
            );
            copy.tables.put(tableCopy.getId(), tableCopy);
        }
        
        return copy;
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
     * Create a sample layout and load tables from database
     */
    public static RestaurantLayout createSampleLayout() {
        RestaurantLayout layout = new RestaurantLayout("Restaurant Layout", "Restaurant table layout");
        layout.loadTablesFromDatabase();
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
    
    public int getNextTableId() {
        return nextTableId;
    }
    
    public void setNextTableId(int nextTableId) {
        this.nextTableId = nextTableId;
    }
}
