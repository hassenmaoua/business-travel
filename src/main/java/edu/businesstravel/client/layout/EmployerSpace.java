package edu.businesstravel.client.layout;

import com.jfoenix.controls.JFXButton;
import edu.businesstravel.client.companion.selection.CompanionSelectionController;
import edu.businesstravel.client.reservation.selection.ReservationSelectionController;
import edu.businesstravel.client.voyage.selection.VoyageSelectionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EmployerSpace extends LayoutController {
    @FXML
    private JFXButton mbCompanion;

    @FXML
    private JFXButton mbReservation;

    @FXML
    private JFXButton mbVoyage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }



    private void setButtonActive(JFXButton button) {
        // Reset background for all buttons
        mbCompanion.getStyleClass().remove("active-menu-button");
        mbReservation.getStyleClass().remove("active-menu-button");
        mbVoyage.getStyleClass().remove("active-menu-button");

        // Set background for the active button
        button.getStyleClass().add("active-menu-button");
    }

    public void navigateReservationSelection(ActionEvent actionEvent) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(ReservationSelectionController.class.getResource("reservation-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbReservation);
    }

    public void navigateVoyageSelection(ActionEvent actionEvent) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(VoyageSelectionController.class.getResource("voyage-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbVoyage);
    }
    public void navigateCompanionsSelection(ActionEvent actionEvent) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(CompanionSelectionController.class.getResource("companion-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbCompanion);
    }


}
