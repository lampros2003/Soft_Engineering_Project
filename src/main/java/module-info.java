module com.analyticsui.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.analyticsui.demo1 to javafx.fxml;
    exports com.analyticsui.demo1;

}
