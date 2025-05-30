package com.callWaiter;

import com.mainpackage.SceneSwitching;
import com.waiter.ManageWaiterScreenClass;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ManageRequestStatusClass {

    private static final List<RequestStatus> requests = new ArrayList<>();
    private final ManageWaiterScreenClass waiterManager;
    private final int tableNumber;

    public ManageRequestStatusClass(int tableNumber) {
        this.tableNumber = tableNumber;
        this.waiterManager = new ManageWaiterScreenClass();
    }

    // Creates new request status for the table
    public void initializeStatus() {
        RequestStatus newStatus = new RequestStatus();
        newStatus.setTableNumber(tableNumber);
        newStatus.setStatus("Pending");
        newStatus.setActive(true);
        requests.add(newStatus);
    }

    // Checks if there is an active request for the specific table
    public boolean checkIfRequested() {
        return requests.stream()
                .anyMatch(r -> r.getTableNumber() == this.tableNumber && r.isActive());
    }

    // Gets the current request status for the specific table
    private RequestStatus getCurrentRequest() {
        return requests.stream()
                .filter(r -> r.getTableNumber() == this.tableNumber)
                .findFirst()
                .orElse(null);
    }

    // Calls the waiter for the specific table
    public void callWaiter() {
        if (checkIfRequested()) {
            System.out.println("Request active for table " + tableNumber);
            return;
        }

        initializeStatus();
//        waiterManager.notifyAllWaiters();
        System.out.println("Waiter called for table " + tableNumber);
    }

    // Cancels the current request for the specific table
    public void cancelRequest() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Cancelled");
            status.setActive(false);
        }
    }

    // Accepts the request by the waiter
    public void waiterAccepted() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Accepted");
        }
    }

    // Marks the request as arrived by the waiter
    public void waiterArrived() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Arrived");
            status.setActive(false);
        }
    }

    // Re-notifies the waiters if the request is still active
    public void reNotify() {
        RequestStatus status = getCurrentRequest();
        if (status != null && "Accepted".equals(status.getStatus())) {
            status.setStatus("Pending");
            status.setActive(true);
//            waiterManager.notifyAllWaiters();
        }
    }
}
