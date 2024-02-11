package edu.businesstravel.client.companion.selection;

import edu.businesstravel.entities.*;
import edu.businesstravel.services.authentification.AuthenticationService;
import edu.businesstravel.services.companion.CompanionService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CompanionSelectionController implements Initializable {
    private final CompanionService companionService;
    private ObservableList<Companion> allCompanion;

    @FXML
    private TableView<Companion> companionTableView;

    @FXML
    private TableColumn<Companion, Date> dateCCol;

    @FXML
    private TableColumn<Companion, String> domaineActiviteCol;

    @FXML
    private TableColumn<Companion, String> employerEmailCol;

    @FXML
    private TableColumn<Companion, String> entrepriseCol;

    @FXML
    private TableColumn<Companion, String> evenementTitreCol;

    @FXML
    private TableColumn<Companion, Long> idCol;

    @FXML
    private TextField searchCompanionField;

    @FXML
    private TableColumn<Companion, String> userNomCol;

    @FXML
    private TableColumn<Companion, String> voyageNomCol;

    public CompanionSelectionController() {
        companionService = new CompanionService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<Companion, Long>("idCompanion"));
        userNomCol.setCellValueFactory(new PropertyValueFactory<Companion, String>("nom"));

        evenementTitreCol.setCellValueFactory(cellData -> {
            Evenement evenement = cellData.getValue().getEvenement();
            if (evenement != null) {
                return new SimpleStringProperty(evenement.getTitre());
            } else {
                return new SimpleStringProperty("--");
            }
        });

        domaineActiviteCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getEmployee();
            if (user != null && user.getEntreprise() != null && user.getEntreprise().getDomaine() != null) {
                return new SimpleStringProperty(user.getEntreprise().getDomaine().getNom());
            } else {
                return new SimpleStringProperty("--");
            }
        });

        voyageNomCol.setCellValueFactory(cellData -> {
            Voyage voyage = cellData.getValue().getVoyage();
            if (voyage != null) {
                return new SimpleStringProperty(voyage.getNom());
            } else {
                return new SimpleStringProperty("None assigné");
            }
        });

        employerEmailCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getEmployee();
            if (user != null) {
                return new SimpleStringProperty(user.getEmail());
            } else {
                return new SimpleStringProperty("--");
            }
        });

        entrepriseCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getEmployee();
            if (user != null && user.getEntreprise() != null) {
                return new SimpleStringProperty(user.getEntreprise().getNomEntreprise());
            } else {
                return new SimpleStringProperty("--");
            }
        });

        User currentUser = AuthenticationService.getLoggedInUser();
        Role role = currentUser.getRole();
        allCompanion = FXCollections.observableList(new ArrayList<>());

        if (role.equals(Role.ADMIN)) {
            allCompanion = FXCollections.observableList(companionService.getAll());
        }
        if (role.equals(Role.COORDINATOR)) {
            // TODO
            allCompanion = FXCollections.observableList(companionService.getAll());
        }

        if (role.equals(Role.EMPLOYER)) {
            // TODO
            allCompanion = FXCollections.observableList(companionService.getAll());
        }

        companionTableView.setItems(allCompanion);

        // Add a listener to the search field
        searchCompanionField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filter the users based on the search criteria
            filterCompanion(newValue);
        });

    }

    private void filterCompanion(String searchText) {
        // If the search text is empty, display all users
        if (searchText == null || searchText.trim().isEmpty()) {
            companionTableView.setItems(allCompanion);
            return;
        }

        // Split the search text into separate keywords
        String[] keywords = searchText.toLowerCase().split("\\s+");

        // Filter the users based on the search criteria
        List<Companion> filteredUsers = allCompanion.stream()
                .filter(companion ->
                        Arrays.stream(keywords).allMatch(keyword ->
                                companion.getNom().toLowerCase().contains(keyword)
                                        || companion.getEvenement().getTitre().toLowerCase().contains(keyword)
                                        || companion.getEmployee().getEntreprise().getDomaine().getNom().toLowerCase().contains(keyword)
                                        || companion.getEmployee().getEmail().toLowerCase().contains(keyword)
                        )
                )
                .collect(Collectors.toList());

        // Update the TableView with the filtered users
        companionTableView.setItems(FXCollections.observableList(filteredUsers));
    }
}
