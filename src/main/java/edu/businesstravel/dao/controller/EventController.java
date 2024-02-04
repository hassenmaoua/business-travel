package edu.businesstravel.dao.controller;

import edu.businesstravel.dao.entities.Category;
import edu.businesstravel.dao.entities.Etat;
import edu.businesstravel.dao.entities.Event;
import edu.businesstravel.dao.repository.CategoryRepository;
import edu.businesstravel.dao.repository.EventRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EventController implements Initializable {

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
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<Object> categoryComboBox;

    @FXML
    private Button submitButton;





    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final EventRepository eventRepository = new EventRepository();
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
        Etat status = Etat.valueOf(statusComboBox.getValue());
        Category category = (Category) categoryComboBox.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate DateDebut =dateDebutPicker.getValue();
        LocalDate DateFin = dateFinPicker.getValue();


        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Region: " + region);
        System.out.println("Adresse: " + adresse);
        System.out.println("Status: " + status);
        System.out.println("Category: " + category);
        System.out.println("date debut: " + DateDebut);
        System.out.println("date fin: " + DateFin);
        Event e=new Event(title,description,region,adresse,status,category.getIdCategory(), dateDebutPicker.getValue(), dateFinPicker.getValue());
          eventRepository.save(e);
        System.out.println("event a ajouter "+e);

     redirectTolistEvent();
    }
    private void redirectTolistEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu.businesstravel/list_event.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Object> categories = categoryRepository.findAll();
        System.out.println(categories);

        ObservableList<Object> observableCategories = FXCollections.observableList(categories);
        System.out.println("categorires from dataBase" + observableCategories);

        categoryComboBox.setItems(observableCategories);


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