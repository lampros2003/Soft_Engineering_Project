package com.waiter;

import java.util.ArrayList;
import java.util.List;

public class ManageWaiterScreenClass {
    private List<waiterViewController> waiters;

    public ManageWaiterScreenClass() {
        waiters = new ArrayList<>();
    }

    public void registerWaiter(waiterViewController waiter) {
        waiters.add(waiter);
    }

    public static void notifyAllWaiters() {
        System.out.println("Notify all waiters");
    }

    public void acceptRequest() {
        System.out.println("A waiter accepted the request.");
    }

    public void waiterArrived() {
        System.out.println("Waiter has arrived at the table.");
    }
}
