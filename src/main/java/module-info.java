module com.analyticsui.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires java.logging;
    
    opens com.analyticsui.demo1 to javafx.fxml;
    exports com.analyticsui.demo1;
    
    // Main package
    opens com.mainpackage to javafx.fxml;
    exports com.mainpackage;
    
    // Login package
    opens com.login to javafx.fxml;
    exports com.login;
    
    // Menu package
    opens com.menu to javafx.fxml;
    exports com.menu;
}
