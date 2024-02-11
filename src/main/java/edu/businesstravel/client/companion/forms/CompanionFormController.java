package edu.businesstravel.client.companion.forms;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.entities.User;
import edu.businesstravel.services.companion.CompanionService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CompanionFormController implements Initializable {
    private final CompanionService companionService;
    private Companion companion;

    // Déclaration des champs de l'IHM
    @FXML
    private Label ageError;

    @FXML
    private TextField ageField;

    @FXML
    private Label besoinsError;

    @FXML
    private TextArea besoinsField;

    @FXML
    private Label contactError;

    @FXML
    private TextField contactField;

    @FXML
    private TextField domaineField;

    @FXML
    private TextField evenementField;

    @FXML
    private Label nomContactError;

    @FXML
    private TextField nomContactField;

    @FXML
    private Label nomError;

    @FXML
    private TextField nomField;

    @FXML
    private Label passportError;

    @FXML
    private TextField passportField;

    @FXML
    private Button submitBtn;

    // Constructeur de la classe
    public CompanionFormController() {
        companionService = new CompanionService();
    }

    // Initialisation de l'IHM
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des messages d'erreur
        ErrInit();

        // Add focus listeners for input fields
        addFocusListener();

    }

    // Initialisation des données du formulaire
    public void initFormData(User user, Evenement evenement) {
        // Vérification des paramètres
        if ((user == null) || (evenement == null)) return;

        // Initialisation de l'objet Companion
        companion = companionService.initCompanion(user, evenement);

        // Remplissage des champs du formulaire avec les données initiales
        domaineField.setText(companion.getDomaineActivite());
        domaineField.setDisable(true);

        evenementField.setText(evenement.getTitre());
        evenementField.setDisable(true);

        nomField.setText(companion.getNom());
        nomField.setDisable(true);

        ageField.setText(companion.getAge().toString());
        ageField.setDisable(true);
    }

    // Masquage des messages d'erreur
    private void ErrInit() {
        nomError.setVisible(false);
        ageError.setVisible(false);
        contactError.setVisible(false);
        nomContactError.setVisible(false);
        passportError.setVisible(false);
        besoinsError.setVisible(false);
    }

    // Validation des champs du formulaire (à implémenter)
    private boolean validateField() {
        boolean cond1 = validateNumUrgent();

        boolean cond2 = validateNomUrgentField();

        boolean cond3 = validateNomUrgentField();

        boolean cond4 = validatePassportField();

        return cond1 && cond2 && cond3 && cond4 ;
    }


    private void addFocusListener() {
        nomContactField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateNomUrgentField();
            }
        });

        contactField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateNumUrgent();
            }
        });

        passportField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validatePassportField();
            }
        });

    }
    private boolean validateNumUrgent() {
        String text = contactField.getText().trim();
        if (text.isEmpty()) {
            contactError.setText("Téléphone vide.");
            contactError.setVisible(true);
            return false;
        } else if (!text.matches("^[24579]\\d{7}$")) {
            contactError.setText("Commencer par 2, 4, 5, 7 ou 9, 8 chiffres.");
            contactError.setVisible(true);
            return false;
        } else {
            contactError.setVisible(false);
            return true;
        }
    }

    private boolean validateNomUrgentField() {
        String text = nomContactField.getText().trim();
        if (text.isEmpty()) {
            nomContactError.setText("Champ est requis.");
            nomContactError.setVisible(true);
            return false;
        } else if (text.length() < 3) {
            nomContactError.setText("Au moins 3 caractères.");
            nomContactError.setVisible(true);
            return false;
        } else if (text.length() > 30) {
            nomContactError.setText("Max. 30 caractères.");
            nomContactError.setVisible(true);
            return false;
        } else if (!text.matches("[a-zA-Z]+")) {
            nomContactError.setText("Lettres uniquement.");
            nomContactError.setVisible(true);
            return false;
        } else {
            nomContactError.setVisible(false);
            return true;
        }
    }

    private boolean validatePassportField() {
        String text = passportField.getText().trim();
        if (text.isEmpty()) {
            passportError.setText("Champ est requis.");
            passportError.setVisible(true);
            return false;
        } else {
            passportError.setVisible(false);
            return true;
        }
    }

    // Gestion de la soumission du formulaire
    public void handleSubmit() {
        // Validation des champs avant la soumission du formulaire
        ;

        // Vérification de la présence d'un objet Companion
        if (companion == null) {
            return;
        }
        boolean isValid = validateField();
        if (isValid) {

            // Attribution des valeurs des champs du formulaire à l'objet Companion
            companion.setContactUrgenceNom(nomContactField.getText());
            companion.setContactUrgenceTel(contactField.getText());
            companion.setNumPassport(passportField.getText());
            companion.setBesoinsSpeciaux(besoinsField.getText());

            // Ajout du Companion en utilisant le service CompanionService
            try {
                companionService.add(companion);
                // Affichage d'une boîte de dialogue de succès
                Alert alert = showAlert(Alert.AlertType.INFORMATION, "Succès", "Enregistrement de l'utilisateur réussi.");
                alert.setOnCloseRequest(event -> {
                    Stage stage = (Stage) submitBtn.getScene().getWindow();
                    // Fermeture de la fenêtre après la confirmation
                    stage.close();
                });
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                // Affichage d'une boîte de dialogue d'erreur en cas d'échec
                showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage()).showAndWait();
            }
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

}
