module com.mainpackage {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens com.payBill.controller to javafx.fxml;
    exports com.payBill.controller;
    exports com.payBill.util;
    exports com.mainpackage;

}
