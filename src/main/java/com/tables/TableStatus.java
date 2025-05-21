package com.tables;

import javafx.geometry.Point2D;
import java.time.LocalTime;

/**
 * Extended table class that includes status information
 */
public class TableStatus {
    private final int id;
    private final Point2D position;
    private final double width;
    private final double height;
    private final int seatingCapacity;
    private TablesScreenController.TableState state;
    private int guestsCount;
    private LocalTime occupiedTime;
    private String serverName;
    private final int columnIndex;
    private final int rowIndex;
    
    public TableStatus(int id, Point2D position, double width, double height, int seatingCapacity, 
                      TablesScreenController.TableState state, int guestsCount, LocalTime occupiedTime, 
                      String serverName, int columnIndex, int rowIndex) {
        this.id = id;
        this.position = position;
        this.width = width;
        this.height = height;
        this.seatingCapacity = seatingCapacity;
        this.state = state;
        this.guestsCount = guestsCount;
        this.occupiedTime = occupiedTime;
        this.serverName = serverName;
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }
    
    public int getId() {
        return id;
    }
    
    public Point2D getPosition() {
        return position;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public int getSeatingCapacity() {
        return seatingCapacity;
    }
    
    public TablesScreenController.TableState getState() {
        return state;
    }
    
    public void setState(TablesScreenController.TableState state) {
        this.state = state;
    }
    
    public boolean isAvailable() {
        return state == TablesScreenController.TableState.AVAILABLE;
    }
    
    public boolean isOccupied() {
        return state == TablesScreenController.TableState.OCCUPIED;
    }
    
    public int getGuestsCount() {
        return guestsCount;
    }
    
    public void setGuestsCount(int guestsCount) {
        this.guestsCount = guestsCount;
    }
    
    public LocalTime getOccupiedTime() {
        return occupiedTime;
    }
    
    public void setOccupiedTime(LocalTime occupiedTime) {
        this.occupiedTime = occupiedTime;
    }
    
    public String getServerName() {
        return serverName;
    }
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public int getRowIndex() {
        return rowIndex;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Table ").append(id).append(": ");
        sb.append("Status: ").append(state);
        
        if (state == TablesScreenController.TableState.OCCUPIED) {
            sb.append(", Guests: ").append(guestsCount);
            sb.append(", Server: ").append(serverName);
            if (occupiedTime != null) {
                sb.append(", Occupied since: ").append(occupiedTime);
            }
        }
        
        return sb.toString();
    }
}
