package com.waiter;

import com.callWaiter.ManageRequestStatusClass;
import javafx.fxml.FXML;

public class waiterViewController {
    @FXML
    private void onAcceptRequest() {
        ManageRequestStatusClass manager = new ManageRequestStatusClass(1);
        manager.waiterAccepted();
    }
}
