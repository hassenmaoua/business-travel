package edu.businesstravel.client.voyage.details;

import edu.businesstravel.entities.Companion;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.entities.User;
import edu.businesstravel.entities.Voyage;
import edu.businesstravel.services.voyage.VoyageService;
import edu.businesstravel.tools.EmailAPI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VoyageDetailsController implements Initializable {
    private final VoyageService voyageService;

    private Voyage voyage;

    @FXML
    private Label avionLabel;

    @FXML
    private Label classeLabel;

    @FXML
    private TableView<Companion> companionTableView;

    @FXML
    private Label dateDepartLabel;

    @FXML
    private Label departLabel;

    @FXML
    private Label destinationLabel;

    @FXML
    private TableColumn<Companion, String> domaineCol;

    @FXML
    private TableColumn<Companion, String> employerNomCol;

    @FXML
    private TableColumn<Companion, String> entrepriseCol;

    @FXML
    private TableColumn<Companion, String> evenementCol;

    @FXML
    private Button matchingBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private Label title;

    public VoyageDetailsController() {
        voyageService = new VoyageService();
    }

    @FXML
    void handleMatching(ActionEvent event) {
        Voyage result = voyageService.matchingCompanions(voyage);
        if (result != null) {
            dateDepartLabel.setText(result.getDateDepart().toString());
            ObservableList<Companion> companionList = fetchCompanions();

            for (Companion c : companionList
            ) {
                String email = c.getEmployee().getEmail();
                EmailAPI.sendEmail(email, c, voyage);
            }

            companionTableView.setItems(companionList);
            matchingBtn.setDisable(true);

        } else {
            System.out.println("Matching failed");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employerNomCol.setCellValueFactory(new PropertyValueFactory<Companion, String>("nom"));

        evenementCol.setCellValueFactory(cellData -> {
            Evenement evenement = cellData.getValue().getEvenement();
            if (evenement != null) {
                return new SimpleStringProperty(evenement.getTitre());
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

        domaineCol.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getEmployee();
            if (user != null && user.getEntreprise() != null && user.getEntreprise().getDomaine() != null) {
                return new SimpleStringProperty(user.getEntreprise().getDomaine().getNom());
            } else {
                return new SimpleStringProperty("--");
            }
        });

        // Call updateUI directly
    }

    private ObservableList<Companion> fetchCompanions() {
        ObservableList<Companion> companions = FXCollections.observableList(new ArrayList<>());
        companions = FXCollections.observableList(voyageService.getCompanions(voyage.getIdVoyage()));
        return companions;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
        updateUI();
    }

    private void updateUI() {
        title.setText("Details Voyage #" + voyage.getIdVoyage());
        nameLabel.setText(voyage.getNom());
        departLabel.setText(voyage.getDepart());
        destinationLabel.setText(voyage.getDestination());
        classeLabel.setText(voyage.getClasse().name());
        avionLabel.setText(voyage.getAvion());

        if (voyage.getDateDepart() != null) {
            dateDepartLabel.setText(voyage.getDateDepart().toString());
        } else {
            dateDepartLabel.setText("--");
        }

        ObservableList<Companion> companions = fetchCompanions();
        companionTableView.setItems(companions);

        if (!companions.isEmpty()) {
            matchingBtn.setDisable(true);
        }
    }
}
