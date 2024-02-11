package edu.businesstravel.client.voyage.selection;

import edu.businesstravel.MainApplication;
import edu.businesstravel.client.user.forms.UserFormController;
import edu.businesstravel.client.voyage.details.VoyageDetailsController;
import edu.businesstravel.client.voyage.forms.VoyageFormController;
import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import edu.businesstravel.entities.Voyage;
import edu.businesstravel.services.authentification.AuthenticationService;
import edu.businesstravel.services.voyage.VoyageService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class VoyageSelectionController implements Initializable {

    private final VoyageService voyageService;

    private ObservableList<Voyage> allVoyages;


    @FXML
    private Button addVoyageBtn;

    @FXML
    private TableColumn<Voyage, String> avionCol;

    @FXML
    private TableColumn<Voyage, String> classeCol;

    @FXML
    private TableColumn<Voyage, Date> dateCCol;

    @FXML
    private TableColumn<Voyage, Date> dateDepartCol;

    @FXML
    private TableColumn<Voyage, Date> departCol;

    @FXML
    private TableColumn<Voyage, String> destinationCol;

    @FXML
    private TableColumn<Voyage, Long> idCol;

    @FXML
    private Button modifierVoyageBtn;

    @FXML
    private TableColumn<Voyage, String> nomCol;

    @FXML
    private TextField searchVoyageField;

    @FXML
    private TableView<Voyage> voyageTableView;

    public VoyageSelectionController() {
        voyageService = new VoyageService();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<Voyage, Long>("idVoyage"));
        nomCol.setCellValueFactory(new PropertyValueFactory<Voyage, String>("nom"));
        dateDepartCol.setCellValueFactory(new PropertyValueFactory<Voyage, Date>("dateDepart"));
        departCol.setCellValueFactory(new PropertyValueFactory<Voyage, Date>("depart"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<Voyage, String>("destination"));
        classeCol.setCellValueFactory(new PropertyValueFactory<Voyage, String>("classe"));
        avionCol.setCellValueFactory(new PropertyValueFactory<Voyage, String>("avion"));
        dateCCol.setCellValueFactory(new PropertyValueFactory<Voyage, Date>("dateC"));


        User currentUser = AuthenticationService.getLoggedInUser();
        Role role = currentUser.getRole();
        allVoyages = FXCollections.observableList(new ArrayList<>());

        if (role.equals(Role.ADMIN)) {
            allVoyages = FXCollections.observableList(voyageService.getAll());
            addVoyageBtn.setVisible(true);
            modifierVoyageBtn.setVisible(true);
        }
        if (currentUser.getEntreprise() != null && role.equals(Role.COORDINATOR)) {
            allVoyages = FXCollections.observableList(voyageService.getAllByEmployeeEntrepriseId(currentUser.getEntreprise().getIdEntreprise()));
            addVoyageBtn.setVisible(false);
            modifierVoyageBtn.setVisible(false);
        }

        if (role.equals(Role.EMPLOYER)) {
            allVoyages = FXCollections.observableList(voyageService.getAllByEmployeeId(currentUser.getIdUser()));
            addVoyageBtn.setVisible(false);
            modifierVoyageBtn.setVisible(false);
        }

        voyageTableView.setItems(allVoyages);

        // Set up row click event handler
        voyageTableView.setOnMouseClicked(this::handleRowClick);

        modifierVoyageBtn.setDisable(true);

        // Add a listener to the selectedItems property of the TableView's selection model
        voyageTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Voyage>) change -> {
            // Enable modifierBtn only if an item is selected
            modifierVoyageBtn.setDisable(change.getList().isEmpty());
        });

        // Add a listener to the search field
        searchVoyageField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filter the users based on the search criteria
            filterVoyage(newValue);
        });

        if (voyageService.isExistNonAffectedCompanions()) {
            addVoyageBtn.setDisable(false);
        } else {
            addVoyageBtn.setDisable(true);
        }

    }

    @FXML
    void handleAddVoyage(ActionEvent event) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(VoyageFormController.class.getResource("voyage-form.fxml")));

            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Voayage");

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

    @FXML
    void handleModifier(ActionEvent event) {
        Voyage selectedVoyage = voyageTableView.getSelectionModel().getSelectedItem();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader;
        Parent root = null;
        try {
            fxmlLoader =new FXMLLoader( Objects.requireNonNull(VoyageFormController.class.getResource("voyage-form.fxml")));
            root = fxmlLoader.load();

            VoyageFormController voyageFormController = fxmlLoader.getController();

            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Voayage");

            voyageFormController.initData(selectedVoyage);

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

    private void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Check for single click
            TableView<Voyage> tableView = (TableView<Voyage>) event.getSource();
            Voyage selectedVoyage = tableView.getSelectionModel().getSelectedItem();

            if (selectedVoyage != null) {
                openVoyageDetails(event, selectedVoyage);
            }
        }
    }

    private void openVoyageDetails(MouseEvent event, Voyage voyage) {
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



    private void filterVoyage(String searchText) {
        // If the search text is empty, display all users
        if (searchText == null || searchText.trim().isEmpty()) {
            voyageTableView.setItems(allVoyages);
            return;
        }

        // Split the search text into separate keywords
        String[] keywords = searchText.toLowerCase().split("\\s+");

        // Filter the users based on the search criteria
        List<Voyage> filteredUsers = allVoyages.stream()
                .filter(voyage ->
                        Arrays.stream(keywords).allMatch(keyword ->
                                voyage.getNom().toLowerCase().contains(keyword)
                                        || voyage.getDestination().toLowerCase().contains(keyword)
                                        || voyage.getDepart().toLowerCase().contains(keyword)
                                        || voyage.getAvion().toLowerCase().contains(keyword)
                        )
                )
                .collect(Collectors.toList());

        // Update the TableView with the filtered users
        voyageTableView.setItems(FXCollections.observableList(filteredUsers));
    }

}
