module com.mainpackage {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens com.invMGMT.controller to javafx.fxml;
    exports com.mainpackage;

}
