module com.example.schedulejawn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.schedulejawn to javafx.fxml;
    exports com.example.schedulejawn;
    exports controller;
    opens controller to javafx.fxml;
    exports model;
    opens model to javafx.base;
}