package edu.businesstravel.client.entreprise.forms;

import edu.businesstravel.entities.Domaine;
import edu.businesstravel.entities.Entreprise;
import edu.businesstravel.repository.EntrepriseRepository;
import edu.businesstravel.tools.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;


public class AjouterEntrepriseController implements Initializable {
    private final EntrepriseRepository entrepriseRepository;
    @FXML
    private TextField nomEntrepriseField;
    @FXML
    private TextField raisonSocialeField;
    @FXML
    private TextField adresseField;
    @FXML
    private ComboBox<Domaine> secteurActiviteComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numeroTelephoneField;
    @FXML
    private TextField nbEmployeeField;

    public AjouterEntrepriseController() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        this.entrepriseRepository = new EntrepriseRepository();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeDomainesComboBox();

    }

    @FXML
    public void ajouterEntreprise() {
        try {
            String nomEntreprise = nomEntrepriseField.getText();
            String raisonSociale = raisonSocialeField.getText().trim();
            String adresse = adresseField.getText();
            Domaine domaineSelectionne = secteurActiviteComboBox.getValue();
            String email = emailField.getText();
            String telephone = numeroTelephoneField.getText();
            Long nbEmployee = Long.valueOf(nbEmployeeField.getText());


            if (nomEntreprise.isEmpty() || raisonSociale.isEmpty() || adresse.isEmpty() || domaineSelectionne == null || email.isEmpty() || telephone.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            Entreprise nouvelleEntreprise = new Entreprise();
            nouvelleEntreprise.setNomEntreprise(nomEntreprise);
            nouvelleEntreprise.setRaisonSociale(raisonSociale);
            nouvelleEntreprise.setAdresse(adresse);
            nouvelleEntreprise.setDomaine(domaineSelectionne);
            nouvelleEntreprise.setEmail(email);
            nouvelleEntreprise.setTelephone(telephone);
            nouvelleEntreprise.setNbEmployee(nbEmployee);

            Optional<Entreprise> savedEntreprise = Optional.ofNullable(entrepriseRepository.save(nouvelleEntreprise));

            if (savedEntreprise.isPresent()) {
                // Success: Close the window or provide feedback to the user
                closeWindow();
            } else {
                showAlert("info", "Duplicate entry 'aziz' for key 'raisonSociale'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log the exception details
            // Show an error message to the user
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) raisonSocialeField.getScene().getWindow();
        stage.close();
    }


    private void initializeDomainesComboBox() {
        // Récupère la liste des domaines depuis la base de donnés

        secteurActiviteComboBox.getItems().addAll(
                Domaine.IT,
                Domaine.FINANCE,
                Domaine.HEALTHCARE,
                Domaine.EDUCATION,
                Domaine.MANUFACTURING,
                Domaine.RETAIL,
                Domaine.TELECOMMUNICATIONS,
                Domaine.ENERGY,
                Domaine.TRANSPORTATION,
                Domaine.ENTERTAINMENT,
                Domaine.CONSULTING,
                Domaine.AUTOMOTIVE,
                Domaine.MEDIA,
                Domaine.REAL_ESTATE,
                Domaine.PHARMACEUTICAL,
                Domaine.SPORTS);

        // Définir la cellule de rendu personnalisée pour afficher le nom du domaine uniquement
        secteurActiviteComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Domaine> call(ListView<Domaine> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Domaine item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNom());
                        }
                    }
                };
            }
        });

        // Définir le StringConverter personnalisé
        secteurActiviteComboBox.setConverter(new StringConverter<Domaine>() {
            @Override
            public String toString(Domaine domaine) {
                return (domaine != null) ? domaine.getNom() : null;
            }

            @Override
            public Domaine fromString(String string) {
                // Vous pouvez implémenter cette méthode si nécessaire
                return null;
            }
        });
    }

}
