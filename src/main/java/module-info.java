module com.mainpackage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.logging;

    // Analytics UI package
    opens com.analytics to javafx.fxml;
    exports com.analytics;

    // Main package
    opens com.mainpackage to javafx.fxml;
    exports com.mainpackage;

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

    // Waiter package
    opens com.waiter to javafx.fxml;
    exports com.waiter;

    // Schedule package
    opens com.schedule to javafx.fxml;
    exports com.schedule;

    opens com.tables to javafx.fxml;
    exports com.tables;

    opens com.payBill.controller to javafx.fxml;
    exports com.payBill.controller;

    opens com.payBill.util to javafx.fxml;
    exports com.payBill.util;

    opens com.adminView to javafx.fxml;
    exports com.adminView;
    exports com.callWaiter;
    opens com.callWaiter to javafx.fxml;
}
