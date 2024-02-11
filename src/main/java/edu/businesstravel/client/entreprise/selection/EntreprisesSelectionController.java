package edu.businesstravel.client.entreprise.selection;

import edu.businesstravel.client.entreprise.forms.AjouterEntrepriseController;
import edu.businesstravel.client.entreprise.forms.EditEntreprieController;
import edu.businesstravel.entities.Domaine;
import edu.businesstravel.entities.Entreprise;
import edu.businesstravel.repository.EntrepriseRepository;
import edu.businesstravel.tools.ExcelExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EntreprisesSelectionController implements Initializable {
    @FXML
    private Button ajouterButton;

    @FXML
    private TextField searchEntrepriseField;

    @FXML
    private TableView<Entreprise> entreprisesTableView;
    @FXML
    private TableColumn<Entreprise, String> nomEntrepriseColumn;

    @FXML
    private TableColumn<Entreprise, String> raisonSocialeColumn;

    @FXML
    private TableColumn<Entreprise, String> adresseColumn;

    @FXML
    private TableColumn<Entreprise, String> secteurActiviteColumn;
    @FXML
    private TableColumn<Entreprise, String> emailColumn;

    @FXML
    private TableColumn<Entreprise, String> telColumn;


    @FXML
    private TableColumn<Entreprise, Void> updateColumn;

    @FXML
    private TableColumn<Entreprise, Void> deleteColumn;

    private EntrepriseRepository entrepriseRepository;

    public EntreprisesSelectionController() {
        this.entrepriseRepository = new EntrepriseRepository();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEntreprises();
        configureTableColumns();
        configureSearchField();
    }

    @FXML
    public void refresh(){
        loadEntreprises();
    }

    private void configureSearchField() {
        // Add a listener to the textProperty of the search field
        searchEntrepriseField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call the filterData method with the new search text
            filterData(newValue);
        });

        // Add a listener to detect when the text is empty and reload all data
        searchEntrepriseField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadEntreprises(); // Reload all data
            }
        });
    }

    private void filterData(String searchText) {
        // Get the list of all enterprises from the TableView
        ObservableList<Entreprise> allEntreprises = entreprisesTableView.getItems();

        // Create a filtered list to hold the matching enterprises
        ObservableList<Entreprise> filteredEntreprises = FXCollections.observableArrayList();

        // If the search text is empty, display all enterprises
        if (searchText.isEmpty()) {
            // Reload all data by resetting the items in the TableView
            filteredEntreprises.addAll(allEntreprises);
            entreprisesTableView.setItems(filteredEntreprises);
        } else {
            // Iterate through all enterprises and add those that match the search text to the filtered list
            for (Entreprise entreprise : allEntreprises) {
                if (entreprise.getNomEntreprise().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredEntreprises.add(entreprise);
                }
            }

            // Set the filtered list as the new items in the TableView
            entreprisesTableView.setItems(filteredEntreprises);
        }
    }






    @FXML
    public void ajouterEntreprise(ActionEvent event) {
        // Add your logic for handling the "Ajouter" button click here
        System.out.println("Ajouter button clicked!");

        // For example, you can open a new window for adding an entreprise
        navigateToAjoutEntreprise();
    }

    private void navigateToAjoutEntreprise() {
        try {
            FXMLLoader loader = new FXMLLoader(AjouterEntrepriseController.class.getResource("ajout-entreprise.fxml"));
            Parent root = loader.load();

            Stage ajoutEntrepriseStage = new Stage();
            ajoutEntrepriseStage.initModality(Modality.APPLICATION_MODAL);
            ajoutEntrepriseStage.setTitle("Ajouter Entreprise");

            Scene ajoutEntrepriseScene = new Scene(root);
            ajoutEntrepriseStage.setScene(ajoutEntrepriseScene);

            ajoutEntrepriseStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void navigateToEditEntreprise(Entreprise entreprise) {
        try {
            FXMLLoader loader = new FXMLLoader(EditEntreprieController.class.getResource("edit-entreprise.fxml"));
            Parent root = loader.load();

            EditEntreprieController editController = loader.getController();
            editController.loadEnterpriseData(entreprise);

            Stage editEntrepriseStage = new Stage();
            editEntrepriseStage.initModality(Modality.APPLICATION_MODAL);
            editEntrepriseStage.setTitle("Edit Entreprise");

            Scene editEntrepriseScene = new Scene(root);
            editEntrepriseStage.setScene(editEntrepriseScene);

            editEntrepriseStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void loadEntreprises() {
        Iterable<Entreprise> entreprises = (List<Entreprise>) entrepriseRepository.findAll();
       // System.out.println(entreprises);
        ObservableList<Entreprise> entreprisesList = FXCollections.observableArrayList();
        entreprises.forEach(entreprisesList::add);
        entreprisesTableView.setItems(entreprisesList);
        //System.out.println(entreprisesTableView);
    }

    private void configureTableColumns() {
        nomEntrepriseColumn.setCellValueFactory(new PropertyValueFactory<>("nomEntreprise"));
        raisonSocialeColumn.setCellValueFactory(new PropertyValueFactory<>("raisonSociale"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        secteurActiviteColumn.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            Domaine domaine = cellData.getValue().getDomaine();
            if (domaine != null) {
                property.setValue(domaine.getNom());
            } else {
                property.setValue("Non spécifié"); // Valeur par défaut si le secteur d'activité n'est pas spécifié
            }
            return property;
        });
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        // ... previous column configurations

        // Customizing the Update column (Edit)
        updateColumn.setCellFactory(column -> {
            TableCell<Entreprise, Void> cell = new TableCell<Entreprise, Void>() {
                private final Button editButton = new Button("Edit");

                {
                    editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                    editButton.setOnAction(event -> {

                        Entreprise entreprise = getTableView().getItems().get(getIndex());
                        navigateToEditEntreprise(entreprise);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };
            return cell;
        });

        // Customizing the Delete column
        deleteColumn.setCellFactory(column -> {
            TableCell<Entreprise, Void> cell = new TableCell<Entreprise, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;"); // Couleur rouge
                    deleteButton.setOnAction(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation Dialog");
                        alert.setHeaderText("Delete Enterprise");
                        alert.setContentText("Are you sure you want to delete this enterprise?");

                        // Show the confirmation dialog and wait for user input
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK){
                            Entreprise entreprise = getTableView().getItems().get(getIndex());
                            entrepriseRepository.deleteById(entreprise.getIdEntreprise());
                            System.out.println("Delete clicked for " + entreprise.getRaisonSociale());

                        }

                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        });
    }

    @FXML
    public void exporterVersExcel() {
        try {
            List<Entreprise> entreprises = entreprisesTableView.getItems();

            // Obtenez le répertoire de téléchargement de l'utilisateur
            String homeDirectory = System.getProperty("user.home");

            // Spécifiez le chemin du fichier dans le répertoire de téléchargement
            String defaultFilePath = homeDirectory + "/Downloads/Entreprises_Data.xlsx";
            File file = new File(defaultFilePath);

            // Exportez vers Excel
            ExcelExporter.exportToExcel(entreprises, file.getAbsolutePath());

            System.out.println("Exportation vers Excel réussie !");
        } catch (Exception e) {
            e.printStackTrace();
            // Gérez toute exception liée à l'exportation vers Excel
            // Affichez un message d'erreur à l'utilisateur si nécessaire
        }
    }


}
