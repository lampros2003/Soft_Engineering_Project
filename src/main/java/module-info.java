module com.mainpackage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.logging;
    requires java.sql;

    // Analytics UI package
    opens com.analytics to javafx.fxml;
    exports com.analytics;

    // Main package
    opens com.mainpackage to javafx.fxml;
    exports com.mainpackage;

    opens com.placeOrder to javafx.fxml;
    exports com.placeOrder;

    // Login package
    opens com.login to javafx.fxml;
    exports com.login;

    // Menu package
    opens com.menu to javafx.fxml;
    exports com.menu;
    opens com.invMGMT.controller to javafx.fxml;

    // Chef package
    opens com.chefView to javafx.fxml;
    exports com.chefView;

    // waiter package
    opens com.waiter to javafx.fxml;
    exports com.waiter;

    //schedule package
    opens com.schedule to javafx.fxml;
    exports com.schedule;

    opens com.tables to javafx.fxml;
    exports com.tables;

    opens com.payBill.controller to javafx.fxml;
    exports com.payBill.controller;

    opens com.payBill.util to javafx.fxml;
    exports com.payBill.util;

    opens com.reservation to javafx.fxml;
    exports com.reservation;


    // Admin View package
    opens com.adminView to javafx.fxml;
    exports com.adminView;

    // Call Waiter package
    exports com.callWaiter to javafx.fxml;
    opens com.callWaiter;

    // Common package
    exports com.common to javafx.fxml;
    opens com.common;

    // Error Handling package
    exports com.errorHandling to javafx.fxml;
    opens com.errorHandling;
}
