package edu.businesstravel.client.evenement.forms;

import edu.businesstravel.entities.Category;
import edu.businesstravel.entities.Etat;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.repository.category.CategoryRepository;
import edu.businesstravel.repository.evenement.EvenementRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AjoutEventController implements Initializable {

    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final EvenementRepository eventRepository = new EvenementRepository();
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;
    @FXML
    private TextField regionTextField;
    @FXML
    private TextField adresseTextField;
    @FXML
    private ComboBox<Etat> statusComboBox;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private Button submitButton;
/*    @FXML
    private void afficherEvenements() {
        List<Object> events = eventRepository.findAll();
        ObservableList<Object> eventList = FXCollections.observableArrayList(events);
        eventTable.setItems(eventList);
    }*/

    @FXML
    private void ajoutEvenement() throws IOException {
        if (isAnyFieldEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return; // Ne pas continuer avec l'ajout
        }
        if (!isDateDebutBeforeDateFin()) {
            showAlert("La date de fin doit être supérieure à la date de début.");
            return; // Ne pas continuer avec l'ajout
        }
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String region = regionTextField.getText();
        String adresse = adresseTextField.getText();
        Etat status = statusComboBox.getValue();
        Category category = (Category) categoryComboBox.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate DateDebut = dateDebutPicker.getValue();
        LocalDate DateFin = dateFinPicker.getValue();


        Evenement e = new Evenement();

        e.setTitre(title);
        e.setDescription(description);
        e.setRegion(region);
        e.setAdresse(adresse);
        e.setEtat(status);
        e.setCategorie(category);
        e.setDateDebut(Date.valueOf(dateDebutPicker.getValue()));
        e.setDateFin(Date.valueOf(dateFinPicker.getValue()));

        eventRepository.save(e);

        showSuccessAlert("Événement ajouté avec succès!");
        clearFormFields();

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();

    }

    private void clearFormFields() {
        titleTextField.clear();
        descriptionTextField.clear();
        regionTextField.clear();
        adresseTextField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        categoryComboBox.getSelectionModel().clearSelection();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categories = categoryRepository.findAll();


        ObservableList<Category> observableCategories = FXCollections.observableList(categories);


        categoryComboBox.setItems(observableCategories);

        statusComboBox.setItems(FXCollections.<Etat>observableList(Arrays.asList(
                Etat.NOUVEAUX, Etat.EN_COURS, Etat.ANNULER, Etat.TERMINER)));

        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return (category != null) ? category.getName() : "";
            }

            @Override
            public Category fromString(String string) {
                // You can implement this method if needed
                // Convert the string back to the Entreprise object
                return null;
            }
        });


    }

    private boolean isAnyFieldEmpty() {
        return titleTextField.getText().isEmpty() ||
                descriptionTextField.getText().isEmpty() ||
                dateDebutPicker.getValue() == null ||
                dateFinPicker.getValue() == null ||
                regionTextField.getText().isEmpty() ||
                adresseTextField.getText().isEmpty() ||
                statusComboBox.getValue() == null ||
                categoryComboBox.getValue() == null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champs obligatoires manquants");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isDateDebutBeforeDateFin() {
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        return dateDebut != null && dateFin != null && dateDebut.isBefore(dateFin);
    }

}