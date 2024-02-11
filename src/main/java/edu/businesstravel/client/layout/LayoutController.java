package edu.businesstravel.client.layout;

import edu.businesstravel.MainApplication;
import edu.businesstravel.client.authentification.LoginController;
import edu.businesstravel.client.dashboard.DashboardController;
import edu.businesstravel.entities.User;
import edu.businesstravel.services.authentification.AuthenticationService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {
    protected double x = 0, y = 0;

    protected User currentUser;

    @FXML
    protected Label titleLabel;

    @FXML
    protected Label nomLabel;

    @FXML
    protected Label emailLabel;

    @FXML
    protected StackPane contentArea;

    @FXML
    protected ImageView exit;


    @FXML
    void pressed(MouseEvent event) {
        y = event.getSceneY();
        x = event.getSceneX();
    }

    @FXML
    void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });


        Parent dashboardFXML = null;
        try {
            dashboardFXML = FXMLLoader.load(Objects.requireNonNull(DashboardController.class.getResource("dashboard-view.fxml")));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(dashboardFXML);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUserData(User user) {
        currentUser = user;
        nomLabel.setText(user.getNom() + " " + user.getPrenom());
        emailLabel.setText(user.getEmail());
    }

    @FXML
    void logout() throws IOException {
        // Create a confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de déconnexion");
        alert.setHeaderText("Êtes-vous sûr de vouloir vous déconnecter ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK, perform logout
            AuthenticationService.logout();

            // Rest of your logout code
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            // Set the stage style to UNDECORATED
            stage.setScene(scene);
            stage.show();
        } else {
            // User clicked Cancel or closed the dialog, do nothing
        }
    }


}
