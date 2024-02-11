package edu.businesstravel.client.layout;

import com.jfoenix.controls.JFXButton;
import edu.businesstravel.client.dashboard.DashboardController;
import edu.businesstravel.client.user.selection.UserSelectionController;
import edu.businesstravel.client.voyage.selection.VoyageSelectionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CoordinatorSpace extends LayoutController {
    @FXML
    private JFXButton mbAccueil;
    @FXML
    private JFXButton mbEmployers;
    @FXML
    private JFXButton mbEvenements;
    @FXML
    private JFXButton mbVoyages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        setButtonActive(mbAccueil);
    }

    private void setButtonActive(JFXButton button) {
        // Reset background for all buttons
        mbAccueil.getStyleClass().remove("active-menu-button");
        mbEmployers.getStyleClass().remove("active-menu-button");
        mbEvenements.getStyleClass().remove("active-menu-button");
        mbVoyages.getStyleClass().remove("active-menu-button");

        // Set background for the active button
        button.getStyleClass().add("active-menu-button");
    }


    public void navigateDashboard(ActionEvent event) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(DashboardController.class.getResource("dashboard-view.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbAccueil);
    }


    public void navigateEmployerSelection(ActionEvent event) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(UserSelectionController.class.getResource("user-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbEmployers);
    }

    public void navigateEvenementSelection(ActionEvent event) throws IOException {
        // TODO implementation de method
        setButtonActive(mbEmployers);
    }


    public void navigateVoyageSelection(ActionEvent event) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(VoyageSelectionController.class.getResource("voyage-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbVoyages);
    }
}
