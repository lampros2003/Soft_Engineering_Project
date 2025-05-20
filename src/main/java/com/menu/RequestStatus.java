package com.menu;

public class RequestStatus {
    private String status;

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
}
