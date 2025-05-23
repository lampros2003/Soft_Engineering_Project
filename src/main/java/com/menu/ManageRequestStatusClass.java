package com.menu;

import com.waiter.ManageWaiterScreenClass;

import java.util.ArrayList;
import java.util.List;

public class ManageRequestStatusClass {
    private List<RequestStatus> requests;
    private ManageWaiterScreenClass waiterManager;
    private RequestStatus requestStatus;
    private int tableNumber;

    public ManageRequestStatusClass(int tableNumber) {
        this.tableNumber = tableNumber;
        this.requests = new ArrayList<>();
        this.waiterManager = new ManageWaiterScreenClass();
    }

    public boolean checkIfRequested() {
        for (RequestStatus r : requests) {
            if (r.getTableNumber() == tableNumber && r.isActive()) {
                return true;
            }
        }
        requestStatus = new RequestStatus(tableNumber, "Pending");
        requests.add(requestStatus);
        return false;
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
