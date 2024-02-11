/**
 * UserFormController class controls the user addition form in a JavaFX application.
 * It validates user input, handles form submission, and interacts with the UserService and EntrepriseService.
 * The class uses JavaFX components such as TextField, ChoiceBox, and DatePicker for user input.
 *
 * @author Hassen MAOUA
 * @version 1.0
 */
package edu.businesstravel.client.user.forms;

import edu.businesstravel.entities.Entreprise;
import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import edu.businesstravel.repository.EntrepriseRepository;
import edu.businesstravel.services.authentification.AuthenticationService;
import edu.businesstravel.services.user.UserService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.text.WordUtils;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class UserFormController implements Initializable {
    private final EntrepriseRepository entrepriseRepository;
    private final UserService userService;

    // Add a callback property
    private Consumer<User> onSubmitCallback;

    private User formData;

    @FXML
    private TextArea adresseField;
    @FXML
    private Label adresseError;

    @FXML
    private DatePicker dateNaissDatePicker;
    @FXML
    private Label dateNaissError;

    @FXML
    private TextField emailField;
    @FXML
    private Label emailError;

    @FXML
    private ChoiceBox<Entreprise> entrepriseSelect;
    @FXML
    private Label entrepriseError;

    @FXML
    private ChoiceBox<Role> roleSelect;
    @FXML
    private Label roleError;

    @FXML
    private TextField nomField;
    @FXML
    private Label nomError;

    @FXML
    private TextField prenomField;
    @FXML
    private Label prenomError;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField telephoneField;
    @FXML
    private Label telephoneError;


    public UserFormController() {
        entrepriseRepository = new EntrepriseRepository();
        userService = new UserService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ErrInit();

        entrepriseSelect.setItems(FXCollections.<Entreprise>observableList((List<Entreprise>) entrepriseRepository.findAll()));

        // Set a custom converter for entrepriseSelect
        entrepriseSelect.setConverter(new StringConverter<Entreprise>() {
            @Override
            public String toString(Entreprise entreprise) {
                return (entreprise != null) ? entreprise.getNomEntreprise() : "";
            }

            @Override
            public Entreprise fromString(String string) {
                // You can implement this method if needed
                // Convert the string back to the Entreprise object
                return null;
            }
        });


        roleSelect.setItems(FXCollections.<Role>observableList(Arrays.asList(
                Role.ADMIN, Role.COORDINATOR, Role.EMPLOYER)));

        // Add focus listeners for input fields
        addFocusListener();

        User user = AuthenticationService.getLoggedInUser();

        if (user.getRole().equals(Role.COORDINATOR)){
            roleSelect.setDisable(true);
            roleSelect.setValue(Role.EMPLOYER);
            entrepriseSelect.setDisable(true);
            entrepriseSelect.setValue(user.getEntreprise());
        }

        // Add listener to roleSelect
        roleSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Role.ADMIN) {
                entrepriseSelect.setDisable(true);
                entrepriseSelect.setValue(null);
            } else {
                entrepriseSelect.setDisable(false);
            }
        });

        // Add listener for submit button
        submitBtn.setOnAction(event -> handleSubmit());
    }

    // Setter for the callback
    public void setOnSubmitCallback(Consumer<User> onSubmitCallback) {
        this.onSubmitCallback = onSubmitCallback;
    }

    private void devInitValue() {
        nomField.setText("Demo");
        prenomField.setText("COM");
        emailField.setText("@email.com");
        dateNaissDatePicker.setValue(LocalDate.of(2000, 1, 1));
        telephoneField.setText("50000000");
    }

    public void initData(Long userId) {
        formData = userService.getById(userId);
        if (formData != null) {
            submitBtn.setText("Modifier");
            nomField.setText(formData.getNom());
            prenomField.setText(formData.getPrenom());
            emailField.setText(formData.getEmail());
            emailField.setDisable(true);
            Date dateN = formData.getDateNaissance();
            adresseField.setText(formData.getAdresse());
            if (dateN != null) {
                dateNaissDatePicker.setValue(dateN.toLocalDate());
            }
            telephoneField.setText(formData.getTelephone());

            if (formData.getEntreprise() != null) {
                entrepriseSelect.setValue(formData.getEntreprise());
            }

            if (formData.getRole() != null) {
                roleSelect.setValue(formData.getRole());
            }
        }
    }

    // Methods for initialization and event handling
    private void ErrInit() {
        nomError.setVisible(false);
        prenomError.setVisible(false);
        emailError.setVisible(false);
        dateNaissError.setVisible(false);
        telephoneError.setVisible(false);
        adresseError.setVisible(false);
        entrepriseError.setVisible(false);
        roleError.setVisible(false);
    }


    // Add a focus listener for each input field
    private void addFocusListener() {
        nomField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateNomField(nomField, nomError);
            }
        });

        prenomField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateNomField(prenomField, prenomError);
            }
        });

        emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateEmailField(emailField, emailError);
            }
        });

        dateNaissDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateDateNaissance(dateNaissDatePicker, dateNaissError);
            }
        });

        telephoneField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateTelephoneField(telephoneField, telephoneError);
            }
        });

        adresseField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateAdresseField(adresseField, adresseError);
            }
        });

        entrepriseSelect.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateEntrepriseSelect(entrepriseSelect, entrepriseError);
            }
        });

        roleSelect.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validateRole(roleSelect, roleError);
            }
        });


    }

    // Validation method for different types of fields
    private void validateField() {
        validateNomField(nomField, nomError);
        validateNomField(prenomField, prenomError);
        validateEmailField(emailField, emailError);
        validateDateNaissance(dateNaissDatePicker, dateNaissError);
        validateTelephoneField(telephoneField, telephoneError);
        validateAdresseField(adresseField, adresseError);
        validateEntrepriseSelect(entrepriseSelect, entrepriseError);
        validateRole(roleSelect, roleError);
    }

    private void validateNomField(TextField argField, Label argError) {
        String text = argField.getText().trim();
        if (text.isEmpty()) {
            argError.setText("Champ est requis.");
            argError.setVisible(true);
        } else if (text.length() < 3) {
            argError.setText("Au moins 3 caractères.");
            argError.setVisible(true);
        } else if (text.length() > 30) {
            argError.setText("Max. 30 caractères.");
            argError.setVisible(true);
        } else if (!text.matches("[a-zA-Z ]+")) {
            argError.setText("Lettres uniquement.");
            argError.setVisible(true);
        } else {
            argError.setVisible(false);
        }
    }

    private void validateEmailField(TextField argField, Label argError) {
        String text = argField.getText().trim();
        if (text.isEmpty()) {
            argError.setText("E-mail est requis.");
            argError.setVisible(true);
        } else if (text.length() < 3) {
            argError.setText("Au moins 3 caractères.");
            argError.setVisible(true);
        } else if (text.length() > 50) {
            argError.setText("Max. 50 caractères.");
            argError.setVisible(true);
        } else if (!text.matches("[A-Za-z0-9+_.-]+@(.+)$")) {
            argError.setText("E-mail non valide.");
            argError.setVisible(true);
        } else if (formData == null && userService.isEmailExist(text)) {
            argError.setText("E-mail déjà utilisé.");
            argError.setVisible(true);
        } else {
            argError.setVisible(false);
        }
    }

//    private void validatePasswordField(PasswordField argField, Label argError) {
//        String text = argField.getText();
//        if (text.isEmpty()) {
//            argError.setText("Mot de passe vide.");
//            argError.setVisible(true);
//        } else if (text.length() < 6) {
//            argError.setText("Au moins 6 caractères.");
//            argError.setVisible(true);
//        } else if (text.length() > 50) {
//            argError.setText("Max. 50 caractères.");
//            argError.setVisible(true);
//        } else if (!text.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$")) {
//            argError.setText("1 maj., 1 min., 1 chiffre, 1 spécial.");
//            argError.setVisible(true);
//        } else {
//            argError.setVisible(false);
//        }
//    }

//    private void validatePasswordConfirmationField(PasswordField argField, PasswordField argCField, Label argError) {
//        validatePasswordField(argCField, argError);
//        String pswdText = argField.getText();
//        String cPswdText = argCField.getText();
//        if (!pswdText.isEmpty()) {
//            if (!cPswdText.equals(pswdText)) {
//                argError.setText("Confirmation ne correspond pas.");
//                argError.setVisible(true);
//            } else {
//                argError.setVisible(false);
//            }
//        }
//    }

    private void validateDateNaissance(DatePicker argDatePicker, Label argError) {
        try {
            LocalDate selectedDate = argDatePicker.getValue();
            LocalDate currentDate = LocalDate.now();
            if (selectedDate != null) {
                if (selectedDate.isAfter(currentDate)) {
                    argError.setText("Date postérieure à aujourd'hui.");
                    argError.setVisible(true);
                } else {
                    argError.setVisible(false);
                }
            } else {
                argError.setText("Sélectionner une date.");
                argError.setVisible(true);
            }
        } catch (Exception e) {
            argError.setText("Format de date invalide.");
            argError.setVisible(true);
        }
    }

    private void validateTelephoneField(TextField argField, Label argError) {
        String text = argField.getText().trim();
        if (text.isEmpty()) {
            argError.setText("Téléphone vide.");
            argError.setVisible(true);
        } else if (!text.matches("^[24579]\\d{7}$")) {
            argError.setText("Commencer par 2, 4, 5, 7 ou 9, 8 chiffres.");
            argError.setVisible(true);
        } else if (formData == null && userService.isPhoneExist(text)) {
            argError.setText("Téléphone déjà enregistré.");
            argError.setVisible(true);
        } else {
            argError.setVisible(false);
        }
    }

    private void validateAdresseField(TextArea argArea, Label argError) {
        String text = argArea.getText().trim();
        if (!text.isEmpty()) {
            if (text.length() < 3) {
                argError.setText("Au moins 3 caractères.");
                argError.setVisible(true);
            } else if (text.length() > 200) {
                argError.setText("Max. 200 caractères.");
                argError.setVisible(true);
            } else {
                argError.setVisible(false);
            }
        } else {
            argError.setVisible(false);
        }
    }

    private void validateEntrepriseSelect(ChoiceBox<Entreprise> argChoice, Label argError) {

        Entreprise entreprise = argChoice.getValue();
        if (roleSelect.getValue() == null) {
            return;
        }

        if ((roleSelect.getValue().equals(Role.COORDINATOR) || roleSelect.getValue().equals(Role.EMPLOYER)) && entreprise == null) {
            argError.setText("Entreprise est requis.");
            argError.setVisible(true);
        } else {
            argError.setVisible(false);
        }

    }

    private void validateRole(ChoiceBox<Role> argChoice, Label argError) {
        Role role = argChoice.getValue();
        if (role == null) {
            argError.setText("Role est requis.");
            argError.setVisible(true);
        } else {
            argError.setVisible(false);
        }
    }

    // Handle submit button action
    private void handleSubmit() {
        // Validate all fields before submitting the form
        validateField();

        User submittedUser;

        if (formData == null) {


            User user = new User();

            // Set values from form fields
            user.setNom(WordUtils.capitalize(nomField.getText()));
            user.setPrenom(prenomField.getText().toUpperCase());
            user.setEmail(emailField.getText().toLowerCase());
            user.setTelephone(telephoneField.getText());
            user.setAdresse(adresseField.getText());
            user.setEntreprise(entrepriseSelect.getValue());
            user.setRole(roleSelect.getValue());

            // Set dateNaissance if it's not null (validate that dateNaissDatePicker is not null)
            if (dateNaissDatePicker.getValue() != null) {
                user.setDateNaissance(java.sql.Date.valueOf(dateNaissDatePicker.getValue()));
            }

            // Add the user using your userService
            try {
                submittedUser = userService.add(user);
                Alert alert = showAlert(Alert.AlertType.INFORMATION, "Success", "Enregistrement de l'utilisateur réussi.");
                alert.setOnCloseRequest(event -> {
                    Stage stage = (Stage) submitBtn.getScene().getWindow();
                    // do what you have to do
                    stage.close();
                });
                alert.showAndWait();
                // Call the callback to notify the parent controller
                if (onSubmitCallback != null) {
                    onSubmitCallback.accept(submittedUser);
                }
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()).showAndWait();
                ;
            }
        } else {

            // Set values from form fields
            formData.setNom(nomField.getText());
            formData.setPrenom(prenomField.getText());
            formData.setEmail(emailField.getText());
            formData.setTelephone(telephoneField.getText());
            formData.setAdresse(adresseField.getText());
            formData.setEntreprise(entrepriseSelect.getValue());
            formData.setRole(roleSelect.getValue());

            // Set dateNaissance if it's not null (validate that dateNaissDatePicker is not null)
            if (dateNaissDatePicker.getValue() != null) {
                formData.setDateNaissance(java.sql.Date.valueOf(dateNaissDatePicker.getValue()));
            }

            // Add the user using your userService
            try {
                submittedUser = userService.update(formData);
                Alert alert = showAlert(Alert.AlertType.INFORMATION, "Success", "Enregistrement de l'utilisateur réussi.");
                alert.setOnCloseRequest(event -> {
                    Stage stage = (Stage) submitBtn.getScene().getWindow();
                    // do what you have to do
                    stage.close();
                });
                alert.showAndWait();
                // Call the callback to notify the parent controller
                if (onSubmitCallback != null) {
                    onSubmitCallback.accept(submittedUser);
                }
            } catch (IllegalArgumentException e) {
                showAlert(Alert.AlertType.ERROR, "Error", e.getMessage()).showAndWait();
                ;
            }


        }


    }

    private Alert showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert;
    }

    private void sendEmail() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

    }


}
