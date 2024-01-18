module edu.businesstravel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.commons.io;


    opens edu.businesstravel to javafx.fxml;
    exports edu.businesstravel;
}