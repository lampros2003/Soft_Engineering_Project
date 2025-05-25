module com.mainpackage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.logging;

    // Analytics UI package
    opens com.analyticsui.demo1 to javafx.fxml;
    exports com.analyticsui.demo1;

    // Main package
    opens com.mainpackage to javafx.fxml;
    opens com.payBill.controller to javafx.fxml;
    exports com.payBill.controller;
    exports com.payBill.util;
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

}
