package edu.businesstravel.dao.controller;

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


}
