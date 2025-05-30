package com.callWaiter;

public class RequestStatus {
    private int tableNumber;
    private String status;
    private boolean active;

    public RequestStatus() {}

    public RequestStatus(int tableNumber, String status, boolean active) {
        this.tableNumber = tableNumber;
        this.status = status;
        this.active = active;
    }

    public void showRequestStatus() {
        System.out.println("Table: " + tableNumber + ", Status: " + status + ", Active: " + active);
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}