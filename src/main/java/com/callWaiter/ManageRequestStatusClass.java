package com.callWaiter;

import com.waiter.ManageWaiterScreenClass;
import java.util.ArrayList;
import java.util.List;

public class ManageRequestStatusClass {

    private static final List<RequestStatus> requests = new ArrayList<>();
    private final ManageWaiterScreenClass waiterManager;
    private final int tableNumber;

    // Constructor με tableNumber (ΑΠΑΡΑΙΤΗΤΟ)
    public ManageRequestStatusClass(int tableNumber) {
        this.tableNumber = tableNumber;
        this.waiterManager = new ManageWaiterScreenClass();
    }

    public void createNewRequest() {
        RequestStatus newStatus = new RequestStatus();
        newStatus.setTableNumber(tableNumber);
        newStatus.setStatus("Pending");
        newStatus.setActive(true);
        requests.add(newStatus);
    }

    // Ελέγχει αν υπάρχει ήδη ενεργό αίτημα για αυτό το τραπέζι
    public boolean checkIfRequested() {
        return requests.stream()
                .anyMatch(r -> r.getTableNumber() == this.tableNumber && r.isActive());
    }

    private RequestStatus getCurrentRequest() {
        return requests.stream()
                .filter(r -> r.getTableNumber() == this.tableNumber)
                .findFirst()
                .orElse(null);
    }

    public void callWaiter() {
        if (checkIfRequested()) {
            System.out.println("Request already active for table " + tableNumber);
            return; // Εδώ μπορείς να εμφανίσεις Alert στο UI
        }

        initializeStatus();
        waiterManager.notifyAllWaiters();
        System.out.println("Waiter called for table " + tableNumber);
    }

    public void cancelRequest() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Cancelled");
            status.setActive(false);
        }
    }

    public void waiterAccepted() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Accepted");
        }
    }

    public void waiterArrived() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Arrived");
            status.setActive(false);
        }
    }

    public void reNotify() {
        RequestStatus status = getCurrentRequest();
        if (status != null && "Accepted".equals(status.getStatus())) {
            status.setStatus("Pending");
            status.setActive(true);
            waiterManager.notifyAllWaiters();
        }
    }
}
