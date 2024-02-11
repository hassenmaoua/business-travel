package edu.businesstravel.client.layout;

import com.jfoenix.controls.JFXButton;
import edu.businesstravel.client.category.CategoryController;
import edu.businesstravel.client.companion.selection.CompanionSelectionController;
import edu.businesstravel.client.dashboard.DashboardController;
import edu.businesstravel.client.entreprise.selection.EntreprisesSelectionController;
import edu.businesstravel.client.evenement.selection.EvenementSelectionController;
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

public class AdminSpace extends LayoutController {
    @FXML
    private JFXButton mbAccueil;

    @FXML
    private JFXButton mbCompanion;

    @FXML
    private JFXButton mbEntreprises;

    @FXML
    private JFXButton mbEvenements;

    @FXML
    private JFXButton mbReservations;

    @FXML
    private JFXButton mbUtilisateurs;

    @FXML
    private JFXButton mbCategory;

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
        mbUtilisateurs.getStyleClass().remove("active-menu-button");
        mbEntreprises.getStyleClass().remove("active-menu-button");
        mbEvenements.getStyleClass().remove("active-menu-button");
        mbReservations.getStyleClass().remove("active-menu-button");
        mbVoyages.getStyleClass().remove("active-menu-button");
        mbCompanion.getStyleClass().remove("active-menu-button");
        mbCategory.getStyleClass().remove("active-menu-button");

        // Set background for the active button
        button.getStyleClass().add("active-menu-button");
    }

    public void navigateDashboard(ActionEvent actionEvent) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(DashboardController.class.getResource("dashboard-view.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbAccueil);
    }

    public void navigateUserSelection(ActionEvent actionEvent) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(UserSelectionController.class.getResource("user-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbUtilisateurs);
    }

    public void navigateEntrepriseSelection(ActionEvent actionEvent) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(EntreprisesSelectionController.class.getResource("entreprise-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbEntreprises);
    }

    public void navigateEvenementSelection(ActionEvent event)throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(EvenementSelectionController.class.getResource("list_event.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbEvenements);
    }

    public void navigateCategorySelection(ActionEvent event)throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(CategoryController.class.getResource("add_category.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbCategory);
    }

    public void navigateReservationSelection(ActionEvent event) {
        // TODO
    }

    public void navigateVoyageSelection(ActionEvent event) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(VoyageSelectionController.class.getResource("voyage-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbVoyages);
    }

    public void navigateCompanionSelection(ActionEvent event) throws IOException {
        Parent p = FXMLLoader.load(Objects.requireNonNull(CompanionSelectionController.class.getResource("companion-selection.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(p);
        setButtonActive(mbCompanion);
    }

}
