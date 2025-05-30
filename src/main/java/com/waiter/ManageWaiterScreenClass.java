package com.waiter;

import java.util.ArrayList;
import java.util.List;

public class ManageWaiterScreenClass {
    private List<waiter_app> waiters;

    public ManageWaiterScreenClass() {
        waiters = new ArrayList<>();
    }

    public void registerWaiter(waiter_app waiter) {
        waiters.add(waiter);
    }

//    public void notifyAllWaiters() {
//        if (waiters.isEmpty()) {
//            System.out.println("No waiters registered to notify.");
//            return;
//        }
//
//        for (waiter_app waiter : waiters) {
//            waiter.displayPopup("Request received at table ");
//        }
//    }

    public void acceptRequest() {
        System.out.println("A waiter accepted the request.");
    }

    public void waiterArrived() {
        System.out.println("Waiter has arrived at the table.");
    }
}
