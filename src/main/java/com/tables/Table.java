package com.tables;

import javafx.geometry.Point2D;

/**
 * Model class representing a restaurant table
 */
public class Table {
    private final int id;
    private final Point2D position;
    private final double width;
    private final double height;
    private final int seatingCapacity;
    private final boolean isAvailable;
    private final int columnIndex;
    private final int rowIndex;
    
    public Table(int id, Point2D position, double width, double height, int seatingCapacity, 
                 boolean isAvailable, int columnIndex, int rowIndex) {
        this.id = id;
        this.position = position;
        this.width = width;
        this.height = height;
        this.seatingCapacity = seatingCapacity;
        this.isAvailable = isAvailable;
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
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public int getRowIndex() {
        return rowIndex;
    }
}
