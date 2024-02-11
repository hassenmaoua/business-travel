module edu.usermanagement {
    // Java modules
    requires java.sql;

    // JavaFX modules for UI components
    requires javafx.controls;
    requires javafx.fxml;

    // External libraries and frameworks
    requires org.apache.commons.io;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires com.fasterxml.jackson.databind;
    requires poi.ooxml;
    requires poi;
    requires org.apache.commons.text;
    requires org.apache.pdfbox;

    opens edu.businesstravel to javafx.fxml;
    exports edu.businesstravel;

    // Opening entites to JavaFX FXML
    exports edu.businesstravel.entities;


    // Opening packages to JavaFX FXML for reflection
    opens edu.businesstravel.client.authentification to javafx.fxml;
    opens edu.businesstravel.client.layout to javafx.fxml;
    opens edu.businesstravel.client.dashboard to javafx.fxml;
    opens edu.businesstravel.client.user.selection to javafx.fxml;
    opens edu.businesstravel.client.user.forms to javafx.fxml;
    opens edu.businesstravel.client.user.details to javafx.fxml;
    opens edu.businesstravel.client.reservation.selection to javafx.fxml;
    opens edu.businesstravel.client.companion.forms to javafx.fxml;
    opens edu.businesstravel.client.companion.selection to javafx.fxml;
    opens edu.businesstravel.client.voyage.selection to javafx.fxml;
    opens edu.businesstravel.client.voyage.forms to javafx.fxml;
    opens edu.businesstravel.client.voyage.details to javafx.fxml;
    opens edu.businesstravel.client.entreprise.forms to javafx.fxml;
    opens edu.businesstravel.client.entreprise.selection to javafx.fxml;
    opens edu.businesstravel.client.evenement.forms to javafx.fxml;
    opens edu.businesstravel.client.evenement.selection to javafx.fxml;
    opens edu.businesstravel.client.category to javafx.fxml;

    // Exporting packages to make them accessible to other modules
    exports edu.businesstravel.client.authentification; // Authentification-related functionality
    exports edu.businesstravel.client.layout; // Layout-related functionality
    exports edu.businesstravel.client.dashboard; // Dashboard-related functionality
    exports edu.businesstravel.client.user.selection; // User selection UI components
    exports edu.businesstravel.client.user.forms; // User forms UI components
    exports edu.businesstravel.client.user.details;
    exports edu.businesstravel.client.reservation.selection; // Reservation selection UI components
    exports edu.businesstravel.client.companion.forms; // Companion forms UI components
    exports edu.businesstravel.client.companion.selection; // Companion forms UI components
    exports edu.businesstravel.client.voyage.selection;
    exports edu.businesstravel.client.voyage.forms;
    exports edu.businesstravel.client.voyage.details;
    exports edu.businesstravel.client.entreprise.forms;
    exports edu.businesstravel.client.entreprise.selection; // Enterprise selection UI components
    exports edu.businesstravel.client.evenement.forms;
    exports edu.businesstravel.client.evenement.selection;
    exports edu.businesstravel.client.category;
}
