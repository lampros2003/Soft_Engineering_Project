package com.menu;

import com.waiter.ManageWaiterWindowClass;

public class ManageRequestStatusClass {
    private RequestStatus requestStatus;
    private ManageWaiterWindowClass waiterManager;

    public ManageRequestStatusClass() {
        this.requestStatus = new RequestStatus();
        this.waiterManager = new ManageWaiterWindowClass();
    }

    public void callWaiter() {
        requestStatus.initializeStatus();
        waiterManager.notifyAllWaiters();
    }

    public void acceptRequest() {
        requestStatus.updateRequestStatusAccepted();
    }

    public void waiterArrived() {
        requestStatus.updateRequestStatusArrived();
    }

    public void cancelRequest() {
        requestStatus.cancel();
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
}
