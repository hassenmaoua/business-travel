package edu.businesstravel.client.authentification;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.businesstravel.client.layout.LayoutController;
import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import edu.businesstravel.services.authentification.AuthenticationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final AuthenticationService authService;
    double x = 0, y = 0;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private AnchorPane signUpSection;
    @FXML
    private JFXTextField signUpEmailField;
    @FXML
    private JFXPasswordField signUpPasswordField;
    @FXML
    private JFXPasswordField signUpConfirmPasswordField;
    @FXML
    private AnchorPane signInSection;
    @FXML
    private JFXTextField signInUsernameField;
    @FXML
    private JFXPasswordField signInPasswordField;
    @FXML
    private Hyperlink forgetPasswordLink;
    @FXML
    private JFXButton LoginButton;
    @FXML
    private Pane loginSliderPane;
    @FXML
    private JFXButton signUpButton;
    @FXML
    private JFXButton signInButton;
    @FXML
    private JFXButton confirmButton;
    @FXML
    private JFXButton cancelButton;


    public LoginController() {
        authService = new AuthenticationService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInUsernameField.setText("demo@admin.com");
        signInPasswordField.setText("admin0000");
    }


    @FXML
    void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        y = event.getSceneY();
        x = event.getSceneX();
    }

    @FXML
    void loginButtonOnAction() {
        String email = signInUsernameField.getText();
        String password = signInPasswordField.getText();
        User user = authService.login(email, password);

        if (user != null) {
            System.out.println("Login successful");

            // Load Layout.fxml using FXMLLoader
            try {
                // Load the appropriate FXML based on the user's role
                String fxmlPath = getFxmlPath(user.getRole());
                FXMLLoader loader = new FXMLLoader(LayoutController.class.getResource(fxmlPath));
                Parent root = loader.load();

                // Create a new scene with the loaded layout
                Scene scene = new Scene(root, 1000, 600);

                // Get the current stage and set the scene
                Stage stage = (Stage) signInUsernameField.getScene().getWindow();
                stage.setScene(scene);

                LayoutController layoutController = loader.getController();
                // You can pass data to the controller if necessary
                layoutController.setUserData(user);

                // Show the stage
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately (show an error message, log, etc.)
            }
        } else {
            System.out.println("Login failed");

            // Display a login failed dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password. Please try again.");

            alert.showAndWait();
        }
    }

    // Utility method to get the FXML path based on the user's role
    private String getFxmlPath(Role userRole) {
        switch (userRole) {
            case ADMIN:
                return "admin-space.fxml";
            case COORDINATOR:
                return "coordinator-space.fxml";
            case EMPLOYER:
                return "employer-space.fxml";
            default:
                // Handle other roles or invalid roles
                return "";
        }
    }

    @FXML
    void close() {
        Platform.exit();
    }


}
