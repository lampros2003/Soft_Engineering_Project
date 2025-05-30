package com.tables;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import javafx.geometry.Point2D;
import com.common.DBManager;

/**
 * RestaurantLayout class that stores and manages all tables in the restaurant.
 * This class acts as a data model for the restaurant layout.
 */
public class RestaurantLayout implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String layoutName;
    private final String description;
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
     * Remove a table from the layout (in memory only)
     */
    public void removeTableInMemory(int tableId) {
        tables.remove(tableId);
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
     * Create a sample layout and load tables from database
     */
    public static RestaurantLayout createSampleLayout() {
        RestaurantLayout layout = new RestaurantLayout("Restaurant Layout", "Restaurant table layout");
        layout.loadTablesFromDatabase();
        return layout;
    }

    // Getters and setters

    public int getTableCount() {
        return tables.size();
    }

}
