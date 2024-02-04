module edu.businesstravel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.io;


    opens edu.businesstravel to javafx.fxml;
    opens edu.businesstravel.dao.controller to javafx.fxml;
    opens edu.businesstravel.dao.entities to javafx.base;

    exports edu.businesstravel;
   exports edu.businesstravel.dao.controller;
}