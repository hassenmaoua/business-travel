module edu.businesstravel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.io;
    requires org.testng;


    opens edu.businesstravel to javafx.fxml;
    exports edu.businesstravel;

    exports edu.businesstravel.controllers;
    opens edu.businesstravel.controllers to javafx.fxml;

}