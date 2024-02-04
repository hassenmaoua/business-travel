package edu.businesstravel.dao.controller;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import edu.businesstravel.dao.entities.Category;
import edu.businesstravel.dao.entities.Etat;
import edu.businesstravel.dao.entities.Event;
import edu.businesstravel.dao.repository.CategoryRepository;
import edu.businesstravel.dao.repository.EventRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.security.auth.callback.Callback;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class List_EventController implements Initializable {
    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TableView<Object> eventTable;

    @FXML
    private TableColumn<Event, String> titleColumn;

    @FXML
    private TableColumn<Event, String> descriptionColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateDebutColumn;

    @FXML
    private TableColumn<Event, LocalDate> dateFinColumn;

    @FXML
    private TableColumn<Event, String> regionColumn;

    @FXML
    private TableColumn<Event, String> adresseColumn;

    @FXML
    private TableColumn<Event, Etat> statusColumn;

    @FXML
    private TableColumn<Event, String> categoryColumn;
    @FXML
    private TextField filterTextField;
    @FXML
    private Pagination pagination;
    private final EventRepository eventRepository = new EventRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();
    private final int itemsPerPage = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        categoryColumn.setCellValueFactory(cellData -> {
            Long categoryId = cellData.getValue().getCategory();
            Category category = (Category) categoryRepository.findById(categoryId);
            String categoryName = (category != null) ? category.getName() : "";
            return new SimpleStringProperty(categoryName);
        });
        // Load data from repository and populate the table
        try {
            List<Object> events = eventRepository.findAll();

            ObservableList<Object> allEventsObservable = FXCollections.observableArrayList(events);

            // Configurer la pagination
            int pageCount = (int) Math.ceil((double) events.size() / itemsPerPage);
            pagination.setPageCount(pageCount);

            // Écouteur pour changer la page
            pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> showTablePage(newIndex.intValue(), allEventsObservable));

            // Afficher la première page
            showTablePage(0, allEventsObservable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterTable() {
        String filterText = filterTextField.getText().toLowerCase();

        List<Object> allEvents = eventRepository.findAll();

        List<Event> filteredEvents = allEvents.stream()
                .filter(obj -> obj instanceof Event)  // Vérifier le type de l'objet
                .map(obj -> (Event) obj)  // Cast vers Event
                .filter(event -> event.getTitle().toLowerCase().contains(filterText) ||
                        event.getDescription().toLowerCase().contains(filterText))
                .collect(Collectors.toList());


        ObservableList<Object> filteredData = FXCollections.observableArrayList(filteredEvents);
        eventTable.setItems(filteredData);

    }
    private void showTablePage(int page, ObservableList<Object> allEvents) {
        int fromIndex = page * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allEvents.size());
        eventTable.setItems(FXCollections.observableArrayList(allEvents.subList(fromIndex, toIndex)));
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
        List<Event> eventsToExport = eventTable.getItems().stream()
                .filter(obj -> obj instanceof Event)
                .map(obj -> (Event) obj)
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
        contentStream.showText("List of Events");
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
        for (Event ev : eventsToExport) {
            contentStream.beginText();
            contentStream.newLineAtOffset(25, yStart);
            contentStream.showText(ev.getTitle());
            contentStream.newLineAtOffset(tableWidth / 7, 0);  // Adjust column widths as needed
            contentStream.showText(ev.getDescription());
            contentStream.showText( "/"+ ev.getDateDebut() + " - " + ev.getDateFin() +"/");
            contentStream.showText( ev.getRegion());
            contentStream.showText( ev.getAdresse());
              contentStream.showText(ev.getStatus().toString());
            contentStream.showText( ev.getCategory().toString());
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
            showAlert("Events exported");

        }
    }
}
