package com.waiter;

import java.util.ArrayList;
import java.util.List;

public class ManageWaiterScreenClass {
    private List<waiterViewController> waiters;

    public ManageWaiterScreenClass() {
        waiters = new ArrayList<>();
    }

    // Καταχώρηση νέου WaiterScreen (αν έχεις πολλούς σερβιτόρους)
    public void registerWaiter(waiterViewController waiter) {
        waiters.add(waiter);
    }

    // Ειδοποίηση όλων των σερβιτόρων
    public void notifyAllWaiters() {
        for (waiterViewController waiter : waiters) {
//            waiter.displayNotification("New request from customer!");
        }
    }

    // Χειρισμός αποδοχής αιτήματος από έναν σερβιτόρο
    public void acceptRequest() {
        System.out.println("A waiter accepted the request.");
    }

    // Όταν ο σερβιτόρος φτάσει στο τραπέζι
    public void waiterArrived() {
        System.out.println("Waiter has arrived at the table.");
    }
}
