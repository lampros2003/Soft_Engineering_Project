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

    // Δημιουργεί νέο αίτημα και το προσθέτει στη static λίστα
    public void initializeStatus() {
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

    // Βρίσκει το αίτημα του συγκεκριμένου τραπεζιού
    private RequestStatus getCurrentRequest() {
        return requests.stream()
                .filter(r -> r.getTableNumber() == this.tableNumber)
                .findFirst()
                .orElse(null);
    }

    // Καλεί σερβιτόρο: Αν δεν υπάρχει ενεργό αίτημα, δημιουργεί και ειδοποιεί
    public void callWaiter() {
        if (checkIfRequested()) {
            System.out.println("Request already active for table " + tableNumber);
            return; // Εδώ μπορείς να εμφανίσεις Alert στο UI
        }

        initializeStatus();
        waiterManager.notifyAllWaiters();
        System.out.println("Waiter called for table " + tableNumber);
    }

    // Ακύρωση αιτήματος
    public void cancelRequest() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Cancelled");
            status.setActive(false);
        }
    }

    // Αποδοχή από σερβιτόρο
    public void waiterAccepted() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Accepted");
        }
    }

    // Ο σερβιτόρος έφτασε
    public void waiterArrived() {
        RequestStatus status = getCurrentRequest();
        if (status != null) {
            status.setStatus("Arrived");
            status.setActive(false);
        }
    }

    // Αν δεν ήρθε ποτέ, ξαναειδοποιεί όλους τους σερβιτόρους
    public void reNotify() {
        RequestStatus status = getCurrentRequest();
        if (status != null && "Accepted".equals(status.getStatus())) {
            status.setStatus("Pending");
            status.setActive(true);
            waiterManager.notifyAllWaiters();
        }
    }
}
