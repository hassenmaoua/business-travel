package edu.businesstravel.controllers;

import edu.businesstravel.HelloApplication;
import edu.businesstravel.HelloController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LayoutController implements Initializable {

    @FXML
    private Button mbDashboard;

    @FXML
    private Button mbDomaines;

    @FXML
    private Button mbEntreprises;

    @FXML
    private Button mbUsers;

    @FXML
    private Label pageTitle;

    @FXML
    private Label profileBtn;

    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void navigateDashboard(ActionEvent actionEvent) throws IOException {
        Parent dashboardFXML = FXMLLoader.load(Objects.requireNonNull(HelloController.class.getResource("view/dashboard-view.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(dashboardFXML);
    }

    public void navigateUserSelection(ActionEvent actionEvent) throws IOException {
        Parent userSelectionView = FXMLLoader.load(Objects.requireNonNull(HelloController.class.getResource("view/users-selection-view.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(userSelectionView);
    }

    public void navigateEntrepriseSelection(ActionEvent actionEvent) throws IOException {
        Parent entrepriseSelectionView = FXMLLoader.load(Objects.requireNonNull(HelloController.class.getResource("view/entreprises-selection-view.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(entrepriseSelectionView);
    }

    public void navigateDomaineSelection(ActionEvent actionEvent) throws IOException {
        Parent domaineSelectionView = FXMLLoader.load(Objects.requireNonNull(HelloController.class.getResource("view/domaines-selection-view.fxml")));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(domaineSelectionView);
    }
}
