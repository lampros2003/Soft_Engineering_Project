package com.callWaiter;

public class RequestStatus {
    private final int tableNumber;
    private String status;

    public RequestStatus(int tableNumber, String pending) {
        this.tableNumber = tableNumber;
        this.initializeStatus();
    }

    public void initializeStatus() {
        status = "Pending";
    }

    public void updateRequestStatusAccepted() {
        status = "Accepted";
    }

    public void updateRequestStatusArrived() {
        status = "Arrived";
    }

    public void cancelRequestStatus() {
        status = "Canceled";
    }

    public String getRequestStatus() {
        return status;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isActive() {
        return status.equals("Pending") || status.equals("Accepted");
    }
}
