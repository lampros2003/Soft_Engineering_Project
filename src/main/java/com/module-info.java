module com.smart_restaurant {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens com.smart_restaurant.controller to javafx.fxml;
    exports com.smart_restaurant;

}
