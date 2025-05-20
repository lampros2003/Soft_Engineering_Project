package com.menu;

import com.waiter.ManageWaiterScreenClass;

public class ManageRequestStatusClass {
    private RequestStatus requestStatus;
    private ManageWaiterScreenClass waiterManager;

    public ManageRequestStatusClass() {
        this.requestStatus = new RequestStatus();
        this.waiterManager = new ManageWaiterScreenClass();
    }

    public boolean checkIfRequested(){
        String status = requestStatus.getRequestStatus();
        if (status!=null){
            return !status.equals("Canceled");
        }
        return false;
    }

    public void callWaiter() {
        requestStatus.initializeStatus();
        waiterManager.notifyAllWaiters();
        startCountingTime();
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void startCountingTime(){

    }

    public void stopCountingTime(){

    }
}
