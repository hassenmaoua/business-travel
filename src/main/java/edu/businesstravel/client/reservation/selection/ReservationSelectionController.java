package edu.businesstravel.client.reservation.selection;

import edu.businesstravel.MainApplication;
import edu.businesstravel.client.companion.forms.CompanionFormController;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.entities.User;
import edu.businesstravel.repository.category.CategoryRepository;
import edu.businesstravel.repository.evenement.EvenementRepository;
import edu.businesstravel.services.authentification.AuthenticationService;
import edu.businesstravel.services.user.UserService;
import edu.businesstravel.tools.GloablStaticData;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReservationSelectionController implements Initializable {
    private final UserService userService;
    private final EvenementRepository evenementRepository;

    public ReservationSelectionController(){
        userService = new UserService();
        evenementRepository = new EvenementRepository();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleSelectReservation(ActionEvent event) {
        Stage stage = new Stage();
        FXMLLoader loader;
        Parent root = null;
        try {
            // Load the appropriate FXML
            loader = new FXMLLoader(CompanionFormController.class.getResource("companion-form.fxml"));
            root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Formulaire");

            // Disable maximize button and resizing
            stage.setResizable(false);

            CompanionFormController companionFormController = loader.getController();
            // You can pass data to the controller if necessary

            User user = AuthenticationService.getLoggedInUser();

            Evenement evenement = evenementRepository.findById(1L).get();
            companionFormController.initFormData(user, evenement);


            // Load and set the window icon
            Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("assets/icons/shield-user.png")));
            stage.getIcons().add(icon);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
