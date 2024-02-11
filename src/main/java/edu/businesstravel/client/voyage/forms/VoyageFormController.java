package edu.businesstravel.client.voyage.forms;

import edu.businesstravel.MainApplication;
import edu.businesstravel.client.voyage.details.VoyageDetailsController;
import edu.businesstravel.entities.Voyage;
import edu.businesstravel.entities.VoyageClasse;
import edu.businesstravel.services.voyage.VoyageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

public class VoyageFormController implements Initializable {
    private final VoyageService voyageService;
    private Voyage voyage;

    @FXML
    private Label avionError;

    @FXML
    private TextField avionField;

    @FXML
    private Label classeError;

    @FXML
    private ChoiceBox<VoyageClasse> classeSelect;

    @FXML
    private Label departError;

    @FXML
    private TextField departField;

    @FXML
    private Label destinationError;

    @FXML
    private ChoiceBox<String> destinationSelect;

    @FXML
    private Label nomError;

    @FXML
    private TextField nomField;

    @FXML
    private Button submitBtn;


    public VoyageFormController() {
        voyageService = new VoyageService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize classes for classeSelect
        classeSelect.getItems().addAll(VoyageClasse.ECONOMY, VoyageClasse.PREMIUM_ECONOMY, VoyageClasse.BUSINESS, VoyageClasse.FIRST);


        Set<String> destinations = voyageService.findAvailableDestinations();

        // Initialize destinations for destinationSelect
        destinationSelect.getItems().addAll(destinations);


        // Initialisation des messages d'erreur
        ErrInit();

        // Add focus listeners for input fields
        addFocusListener();

    }

    private void ErrInit() {
        nomError.setVisible(false);
        departError.setVisible(false);
        destinationError.setVisible(false);
        avionError.setVisible(false);
        classeError.setVisible(false);
    }

    // Validation des champs du formulaire (à implémenter)
    private boolean validateField() {
        boolean cond1 = validateNomField();

        boolean cond2 = validateDepartField();

        boolean cond3 = validateDestinationSelection();

        boolean cond4 = validateClasseSelection();

        boolean cond5 = validateAvionField();

        return cond1 && cond2 && cond3 && cond4 && cond5;
    }

    private boolean validateAvionField() {
        String text = avionField.getText().trim();
        if (text.isEmpty()) {
            avionError.setText("Avion est requis");
            avionError.setVisible(true);
            return false;
        } else {
            avionError.setVisible(false);
            return true;
        }
    }

    private boolean validateClasseSelection() {
        VoyageClasse selectedClasse = classeSelect.getValue();
        if (selectedClasse == null) {
            classeError.setText("Classe est requise");
            classeError.setVisible(true);
            return false;
        } else {
            classeError.setVisible(false);
            return true;
        }
    }

    private boolean validateDestinationSelection() {
        String selectedDestination = (String) destinationSelect.getValue(); // Assuming destinationSelect holds String values
        if (selectedDestination == null || selectedDestination.trim().isEmpty()) {
            destinationError.setText("Destination est requise");
            destinationError.setVisible(true);
            return false;
        } else {
            destinationError.setVisible(false);
            return true;
        }
    }

    private boolean validateDepartField() {
        String text = departField.getText().trim();
        if (text.isEmpty()) {
            departError.setText("Depart est requis");
            departError.setVisible(true);
            return false;
        } else {
            departError.setVisible(false);
            return true;
        }
    }

    private boolean validateNomField() {
        String text = nomField.getText().trim();
        if (text.isEmpty()) {
            nomError.setText("Nom est requis");
            nomError.setVisible(true);
            return false;
        } else {
            nomError.setVisible(false);
            return true;
        }
    }

    private void addFocusListener() {
        nomField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateNomField();
            }
        });

        departField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateDepartField();
            }
        });

        destinationSelect.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateDestinationSelection();
            }
        });

        avionField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateAvionField();
            }
        });

        classeSelect.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateClasseSelection();
            }
        });

    }

    public void handleSubmit(ActionEvent event) {
        // Validation des champs avant la soumission du formulaire
        ;

        // Vérification de la présence d'un objet Companion
//        if (voyage == null) {
//            return;
//        }

        if (voyage == null) {
            voyage = new Voyage();
        }

        boolean isValid = validateField();
        if (isValid) {

            // Attribution des valeurs des champs du formulaire à l'objet Companion
            voyage.setNom(nomField.getText());
            voyage.setDestination(destinationSelect.getValue());
            voyage.setDepart(departField.getText());
            voyage.setAvion(avionField.getText());
            voyage.setClasse(classeSelect.getValue());


            // Ajout du Companion en utilisant le service CompanionService
            try {
                Voyage result = voyageService.add(voyage);
                // Affichage d'une boîte de dialogue de succès
                Alert alert = showAlert(Alert.AlertType.INFORMATION, "Succès", "Enregistrement de voyage réussi.");
                alert.setOnCloseRequest(e -> {
                    Stage stage = (Stage) submitBtn.getScene().getWindow();
                    // Fermeture de la fenêtre après la confirmation
                    stage.close();
                });
                alert.showAndWait();

                // TODO Matching here
                openVoyageDetails(event, result);

            } catch (IllegalArgumentException e) {
                // Affichage d'une boîte de dialogue d'erreur en cas d'échec
                showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage()).showAndWait();
            }
        }
    }


    private void openVoyageDetails(ActionEvent event, Voyage voyage) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(VoyageDetailsController.class.getResource("voyage-details.fxml")));

            root = loader.load();

            // Access the controller and set the Voyage object
            VoyageDetailsController controller = loader.getController();
            controller.setVoyage(voyage);


            stage.setScene(new Scene(root));
            stage.setTitle("Voyage details");

            // Disable maximize button and resizing
            stage.setResizable(false);


            // Load and set the window icon
            Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("assets/icons/airplane-square.png")));
            stage.getIcons().add(icon);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Affichage d'une boîte de dialogue générique
    private Alert showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert;
    }

    public void initData(Voyage selectedVoyage) {
        voyage = selectedVoyage;

        if (voyage != null) {
            submitBtn.setText("Modifier");
            nomField.setText(voyage.getNom());
            departField.setText(voyage.getDepart());
            destinationSelect.setValue(voyage.getDestination());
            destinationSelect.setDisable(true);
            avionField.setText(voyage.getAvion());
            classeSelect.setValue(voyage.getClasse());
        }
    }
}
