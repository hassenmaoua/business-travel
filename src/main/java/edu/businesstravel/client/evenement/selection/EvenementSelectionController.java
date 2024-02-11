package edu.businesstravel.client.evenement.selection;


import edu.businesstravel.MainApplication;
import edu.businesstravel.client.evenement.forms.AjoutEventController;
import edu.businesstravel.client.evenement.forms.ModifEventController;
import edu.businesstravel.entities.Category;
import edu.businesstravel.entities.Etat;
import edu.businesstravel.entities.Evenement;
import edu.businesstravel.repository.category.CategoryRepository;
import edu.businesstravel.repository.evenement.EvenementRepository;
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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EvenementSelectionController implements Initializable {
    private final EvenementRepository eventRepository = new EvenementRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final int itemsPerPage = 15;
    @FXML
    private BorderPane bp;
    @FXML
    private Button exportButton1;
    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TableView<Object> eventTable;
    @FXML
    private TableColumn<Evenement, String> titleColumn;
    @FXML
    private TableColumn<Evenement, String> descriptionColumn;
    @FXML
    private TableColumn<Evenement, LocalDate> dateDebutColumn;
    @FXML
    private TableColumn<Evenement, LocalDate> dateFinColumn;
    @FXML
    private TableColumn<Evenement, String> regionColumn;
    @FXML
    private TableColumn<Evenement, String> adresseColumn;
    @FXML
    private TableColumn<Evenement, Etat> statusColumn;
    @FXML
    private TableColumn<Evenement, String> categoryColumn;
    @FXML
    private TextField filterTextField;
    @FXML
    private Pagination pagination;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        categoryColumn.setCellValueFactory(cellData -> {
            Category category = cellData.getValue().getCategorie();

            if (category != null) {
                return new SimpleStringProperty(category.getName());
            } else {
                return new SimpleStringProperty("--");
            }
        });


        // Load data from repository and populate the table
        try {
            List<Object> events = eventRepository.findAll();

            ObservableList<Object> allEvenementsObservable = FXCollections.observableArrayList(events);

            // Configurer la pagination
            int pageCount = (int) Math.ceil((double) events.size() / itemsPerPage);
            pagination.setPageCount(pageCount);

            // Écouteur pour changer la page
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> showTablePage(newIndex.intValue(), allEvenementsObservable));

            // Afficher la première page
            showTablePage(0, allEvenementsObservable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterTable() {
        String filterText = filterTextField.getText().toLowerCase();

        List<Object> allEvenements = eventRepository.findAll();

        List<Evenement> filteredEvenements = allEvenements.stream()
                .filter(obj -> obj instanceof Evenement)  // Vérifier le type de l'objet
                .map(obj -> (Evenement) obj)  // Cast vers Evenement
                .filter(event -> event.getTitre().toLowerCase().contains(filterText) ||
                        event.getDescription().toLowerCase().contains(filterText))
                .collect(Collectors.toList());


        ObservableList<Object> filteredData = FXCollections.observableArrayList(filteredEvenements);
        eventTable.setItems(filteredData);

    }

    private void showTablePage(int page, ObservableList<Object> allEvenements) {
        int fromIndex = page * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allEvenements.size());
        eventTable.setItems(FXCollections.observableArrayList(allEvenements.subList(fromIndex, toIndex)));
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(message);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void exportToPDF(ActionEvent event) throws IOException {
        // 1. Get the visible events from the table, limiting to 5
        List<Evenement> eventsToExport = eventTable.getItems().stream()
                .filter(obj -> obj instanceof Evenement)
                .map(obj -> (Evenement) obj)
                .limit(5)  // Limit to 5 events
                .collect(Collectors.toList());

        // 2. Create a PDF document
        PDDocument document = new PDDocument();

        // 3. Create a page
        PDPage page = new PDPage(PDRectangle.A2);
        document.addPage(page);

        // 4. Create a content stream
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // 5. Set font and color
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setNonStrokingColor(PDDeviceRGB.INSTANCE.getInitialColor());

        // 6. Write headers
        contentStream.beginText();
        contentStream.newLineAtOffset(25, 750);
        contentStream.showText("List of Evenements");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Date: " + LocalDate.now());
        contentStream.endText();

        // 7. Create table headers
        float tableWidth = page.getMediaBox().getWidth() - 50;  // Adjust width as needed
        float yStart = 600;  // Adjust starting position as needed
        float rowHeight = 20;  // Adjust row height as needed

        String[] headers = {"Title", "Description", "Date", "Region", "Address", "Status", "Category"};
        contentStream.beginText();
        contentStream.newLineAtOffset(25, yStart);
        for (String header : headers) {
            float headerWidth = tableWidth / headers.length;
            contentStream.showText(header);
            contentStream.newLineAtOffset(headerWidth, 0);
        }
        contentStream.endText();

        // 8. Draw table lines
        contentStream.moveTo(25, yStart - rowHeight);
        contentStream.lineTo(tableWidth + 25, yStart - rowHeight);
        contentStream.moveTo(25, yStart - 2 * rowHeight);
        contentStream.lineTo(tableWidth + 25, yStart - 2 * rowHeight);
        contentStream.stroke();

        // 9. Write event details in table rows
        yStart -= 3 * rowHeight;
        for (Evenement ev : eventsToExport) {
            contentStream.beginText();
            contentStream.newLineAtOffset(25, yStart);
            contentStream.showText(ev.getTitre());
            contentStream.newLineAtOffset(tableWidth / 7, 0);  // Adjust column widths as needed
            contentStream.showText(ev.getDescription());
            contentStream.showText("/" + ev.getDateDebut() + " - " + ev.getDateFin() + "/");
            contentStream.showText(ev.getRegion());
            contentStream.showText(ev.getAdresse());
            contentStream.showText(ev.getEtat().toString());
            contentStream.showText(ev.getCategorie().toString());
            contentStream.endText();

            yStart -= rowHeight;
        }

        // 10. Close the content stream
        contentStream.close();

        // 11. Prompt user for file name and save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");

        // Ensure filename has .pdf extension
        fileChooser.setInitialFileName("events.pdf");  // Set default filename with .pdf
        File file = fileChooser.showSaveDialog(eventTable.getScene().getWindow());

        if (file != null) {
            // Handle cases where user doesn't add .pdf extension
            if (!file.getName().endsWith(".pdf")) {
                file = new File(file.getPath() + ".pdf");  // Append .pdf if missing
            }
            document.save(file);
            document.close();
            showAlert("Evenements exported");

        }
    }

    @FXML
    private void supprimer(ActionEvent event) {
        // Récupérer la ligne sélectionnée
        Evenement selectedEvenement = (Evenement) eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvenement != null) {
            Long selectedEvenementId = selectedEvenement.getIdEvenement();

            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer l'événement sélectionné?");
            alert.setContentText("Cette action est irréversible.");

            // Attendre la réponse de l'utilisateur
            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            // Si l'utilisateur a confirmé, supprimer l'événement
            if (result == ButtonType.OK) {
                eventRepository.deleteOne(selectedEvenementId);
                refreshTableAndPagination();
                // Rafraîchir la table après la suppression (à remplacer par votre logique réelle)
                // par exemple, eventTable.getItems().remove(selectedEvenement);
            }
        } else {
            // Aucun événement sélectionné, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun événement sélectionné");
            alert.setContentText("Veuillez sélectionner un événement à supprimer.");
            alert.showAndWait();
        }
    }

    private void refreshTableAndPagination() {
        try {
            List<Object> events = eventRepository.findAll();

            ObservableList<Object> allEvenementsObservable = FXCollections.observableArrayList(events);

            // Configurer la pagination
            int pageCount = (int) Math.ceil((double) events.size() / itemsPerPage);
            pagination.setPageCount(pageCount);

            // Écouteur pour changer la page
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> showTablePage(newIndex.intValue(), allEvenementsObservable));
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> showTablePage(newIndex.intValue(), allEvenementsObservable));

            // Afficher la première page
            showTablePage(pagination.getCurrentPageIndex(), allEvenementsObservable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modifier(ActionEvent event) {
        // Get the selected item from the table
        Evenement selectedItem = (Evenement) eventTable.getSelectionModel().getSelectedItem();
        System.out.println("selectedItem  " + selectedItem);
        if (selectedItem != null) {
            Stage stage = new Stage();
            Parent root = null;
            FXMLLoader loader;

            try {
                loader = new FXMLLoader(ModifEventController.class.getResource("modif_event.fxml"));
                root = loader.load();

                ModifEventController modifEventController = loader.getController();

                // Pass the selected item to the modification form controller
                modifEventController.initializeData(selectedItem);


                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Evenement");

                stage.setResizable(false);
                try (InputStream stream = MainApplication.class.getResourceAsStream("assets/icons/shield-user.png")) {
                    Image icon = new Image(stream);
                    stage.getIcons().add(icon);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(eventTable.getScene().getWindow());
                stage.show();
            } catch (IOException e) {
                // Display a message indicating that no item is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucun événement sélectionné");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un événement à modifier.");
                alert.showAndWait();
            }


        }
    }


    @FXML
    void add(ActionEvent event) {
        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader;

        try {
            loader = new FXMLLoader(AjoutEventController.class.getResource("add_event.fxml"));
            root = loader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Evenement");

            stage.setResizable(false);
            try (InputStream stream = MainApplication.class.getResourceAsStream("assets/icons/shield-user.png")) {
                Image icon = new Image(stream);
                stage.getIcons().add(icon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventTable.getScene().getWindow());
            stage.show();


        } catch (IOException e) {
            // Display a message indicating that no item is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun événement sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un événement à modifier.");
            alert.showAndWait();
        }

    }

    public void loadPage(String page) {
        Parent root = null;

        try {
            //      FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add_category.fxml"));

            root = FXMLLoader.load(getClass().getResource(page + ".fxml"));

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);

        }
        bp.setCenter(root);
    }

    public void ModificationEvenement(javafx.scene.input.MouseEvent mouseEvenement) {
        loadPage("/edu/businesstravel/modif_event");
    }
}
