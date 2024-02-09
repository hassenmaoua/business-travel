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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Modif_Controller implements Initializable {

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
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<Object> categoryComboBox;

    @FXML
    public Button submitButton;
    CategoryRepository categoryRepository=new CategoryRepository();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    EventRepository eventRepository=new EventRepository();

public  void modifier() throws IOException {
    Event e= new Event();
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
    alert.setContentText("Event modified successfully!");
    alert.showAndWait();
      e.setTitle(titleTextField.getText());
      e.setDescription(descriptionTextField.getText());
      e.setAdresse(adresseTextField.getText());
      e.setStatus(Etat.valueOf(statusComboBox.getValue()));
      e.setDateDebut(dateDebutPicker.getValue());
      e.setDateFin(dateFinPicker.getValue());
    Category c= (Category) categoryComboBox.getValue();
      e.setCategory( c.getIdCategory());
      e.setRegion(regionTextField.getText());
      e.setIdEvent(Long.valueOf(idTextField.getText()));
    System.out.println("---------------------------------------------------");
    System.out.println(e);

    eventRepository.update(e,e.getIdEvent());
  redirectTolistEvent();





    }
    public void annuler(){
        redirectTolistEvent();

    }

    @FXML

    public Event initializeData(Event selectedItem) {
        List<Object> categories = categoryRepository.findAll();
        System.out.println(categories);

        ObservableList<Object> observableCategories = FXCollections.observableList(categories);
        System.out.println("categorires from dataBase" + observableCategories);
        System.out.println("item selecteed "+selectedItem);
     categoryComboBox.setItems(observableCategories);
        // Populate the modification form fields with data from the selected item
   titleTextField.setText(String.valueOf(selectedItem.getTitle()));
        descriptionTextField.setText(String.valueOf(selectedItem.getDescription()));
        LocalDate datedebut=selectedItem.getDateDebut();
        dateDebutPicker.setValue(Date.valueOf(selectedItem.getDateDebut()).toLocalDate());
        LocalDate dateFin=selectedItem.getDateFin();
        dateFinPicker.setValue(Date.valueOf(selectedItem.getDateFin()).toLocalDate());
        regionTextField.setText(String.valueOf(selectedItem.getRegion()));
        adresseTextField.setText(String.valueOf(selectedItem.getAdresse()));
        statusComboBox.setValue(String.valueOf(selectedItem.getStatus()));
        categoryComboBox.setValue(String.valueOf(selectedItem.getCategory()));
        idTextField.setText(String.valueOf(Long.valueOf(selectedItem.getIdEvent())));

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

    private void redirectTolistEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/businesstravel/home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1500, 700);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
