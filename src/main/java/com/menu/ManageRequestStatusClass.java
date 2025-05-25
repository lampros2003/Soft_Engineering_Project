package com.menu;

import com.waiter.ManageWaiterScreenClass;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ManageRequestStatusClass {
    private List<RequestStatus> requests;
    private ManageWaiterScreenClass waiterManager;
    private RequestStatus requestStatus;
    private final int tableNumber;

    public ManageRequestStatusClass(int tableNumber) {
        requests = new ArrayList<>();
        this.waiterManager = new ManageWaiterScreenClass();
        this.tableNumber = tableNumber;
    }

    public boolean checkIfRequested() {
        for (RequestStatus r : requests) {
            if (r.getTableNumber() == this.tableNumber && r.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void createNewRequest() {
        requestStatus = new RequestStatus(this.tableNumber, "Pending");
        requests.add(requestStatus);
    }

    public void callWaiter() {
        requestStatus.initializeStatus();
        waiterManager.notifyAllWaiters();
        startCountingTime();
    }

    public String getRequestStatus() {
        return requestStatus.getRequestStatus();
    }

    public void startCountingTime(){

    }

    public void stopCountingTime(){

    }
}
