package edu.businesstravel.client.evenement.forms;

import edu.businesstravel.entities.*;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ModifEventController implements Initializable {

    @FXML
    public Button submitButton;
    CategoryRepository categoryRepository = new CategoryRepository();
    EvenementRepository eventRepository = new EvenementRepository();
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
    private TextField idTextField;
    @FXML
    private ComboBox<Etat> statusComboBox;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

    public void modifier() throws IOException {
        Evenement e = new Evenement();
        if (isAnyFieldEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return; // Ne pas continuer avec l'ajout
        }
        if (!isDateDebutBeforeDateFin()) {
            showAlert("La date de fin doit être supérieure à la date de début.");
            return; // Ne pas continuer avec l'ajout
        }
        // Display a message to indicate successful modification
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modification Success");
        alert.setHeaderText("Modification ");
        alert.setContentText("Evenement modified successfully!");
        alert.showAndWait();
        e.setTitre(titleTextField.getText());
        e.setDescription(descriptionTextField.getText());
        e.setAdresse(adresseTextField.getText());
        e.setEtat(statusComboBox.getValue());
        e.setDateDebut(java.sql.Date.valueOf(dateDebutPicker.getValue()));
        e.setDateFin(java.sql.Date.valueOf(dateFinPicker.getValue()));
        Category c = (Category) categoryComboBox.getValue();
        e.setCategorie(c);
        e.setRegion(regionTextField.getText());
        e.setIdEvenement(Long.valueOf(idTextField.getText()));
        System.out.println("---------------------------------------------------");
        System.out.println(e);

        eventRepository.update(e, e.getIdEvenement());

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();


    }

    public void annuler() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    public Evenement initializeData(Evenement selectedItem) {
        List<Category> categories = categoryRepository.findAll();
        System.out.println(categories);

        ObservableList<Category> observableCategories = FXCollections.observableList(categories);
        System.out.println("categorires from dataBase" + observableCategories);
        System.out.println("item selecteed " + selectedItem);
        categoryComboBox.setItems(observableCategories);
        // Populate the modification form fields with data from the selected item
        titleTextField.setText(String.valueOf(selectedItem.getTitre()));
        descriptionTextField.setText(String.valueOf(selectedItem.getDescription()));

        dateDebutPicker.setValue(selectedItem.getDateDebut().toLocalDate());
        dateFinPicker.setValue(selectedItem.getDateFin().toLocalDate());

        regionTextField.setText(String.valueOf(selectedItem.getRegion()));
        adresseTextField.setText(String.valueOf(selectedItem.getAdresse()));
        statusComboBox.setValue(selectedItem.getEtat());
        categoryComboBox.setValue(selectedItem.getCategorie());
        idTextField.setText(String.valueOf(Long.valueOf(selectedItem.getIdEvenement())));

        return selectedItem;
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

    private boolean isDateDebutBeforeDateFin() {
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();
        return dateDebut != null && dateFin != null && dateDebut.isBefore(dateFin);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champs obligatoires manquants");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
