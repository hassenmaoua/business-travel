package edu.businesstravel.client.user.selection;


import edu.businesstravel.MainApplication;
import edu.businesstravel.client.user.details.UserDetailsController;
import edu.businesstravel.client.user.forms.UserFormController;
import edu.businesstravel.client.voyage.details.VoyageDetailsController;
import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import edu.businesstravel.entities.Voyage;
import edu.businesstravel.services.authentification.AuthenticationService;
import edu.businesstravel.services.user.UserService;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class UserSelectionController implements Initializable {

    private final UserService userService;

    private ContextMenu contextMenu;

    @FXML
    private Button addUserBtn;
    @FXML
    private TableColumn<User, Long> idColumn;
    @FXML
    private TableColumn<User, String> nomColumn;
    @FXML
    private TableColumn<User, String> prenomColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, Role> roleColumn;
    @FXML
    private TableColumn<User, String> entrepriseColumn;
    @FXML
    private TableColumn<User, Date> dateCColumn;
    @FXML
    private TableColumn<User, Date> dateUColumn;
    @FXML
    private Button modifierBtn;
    @FXML
    private TextField searchUserField;
    @FXML
    private TableView<User> usersTableView;

    private ObservableList<User> allUsers;

    public UserSelectionController() {
        userService = new UserService();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeColumns();
        initializeTableData();
        initializeContextMenu();
        initializeEventHandlers();
    }

    private void initializeColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        dateCColumn.setCellValueFactory(new PropertyValueFactory<>("dateC"));
        dateUColumn.setCellValueFactory(new PropertyValueFactory<>("dateU"));
        entrepriseColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEntreprise() != null ?
                        cellData.getValue().getEntreprise().getNomEntreprise() : "--"));

        modifierBtn.setDisable(true);

        usersTableView.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<User>) change -> modifierBtn.setDisable(change.getList().isEmpty()));

        searchUserField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
    }

    private void initializeTableData() {
        User currentUser = AuthenticationService.getLoggedInUser();
        Role userRole = AuthenticationService.getUserRole();

        if (currentUser.getEntreprise() != null && userRole.equals(Role.COORDINATOR)) {
            allUsers = FXCollections.observableList(userService.getByEntrepriseId(currentUser.getEntreprise().getIdEntreprise()));
        } else if (userRole.equals(Role.ADMIN)) {
            allUsers = FXCollections.observableList(userService.getAll());
        } else {
           addUserBtn.setVisible(false);
           modifierBtn.setVisible(false);
            allUsers = FXCollections.observableList(Collections.emptyList());
        }

        usersTableView.setItems(allUsers);

        usersTableView.setOnMouseClicked(this::handleRowClick);
    }

    private void initializeContextMenu() {
        usersTableView.setOnContextMenuRequested(event -> {
            TableView<User> tableView = (TableView<User>) event.getSource();
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                createContextMenu(selectedUser, event.getScreenX(), event.getScreenY()).show(tableView, event.getScreenX(), event.getScreenY());
            }
        });

        Platform.runLater(() -> {
            Scene scene = usersTableView.getScene();
            if (scene != null) {
                Stage stage = (Stage) scene.getWindow();
                stage.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
                        if (contextMenu != null && contextMenu.isShowing()) {
                            contextMenu.hide();
                        }
                    }
                });
            }
        });
    }

    private void initializeEventHandlers() {
        addUserBtn.setOnAction(this::handleAddUser);
        modifierBtn.setOnAction(this::handleModifier);
    }

    private void handleRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TableView<User> tableView = (TableView<User>) event.getSource();
            User selectedUser = tableView.getSelectionModel().getSelectedItem();

            if (selectedUser != null) {
                openUserDetails(selectedUser);
            }
        }
    }

    private ContextMenu createContextMenu(User selectedUser, double screenX, double screenY) {
        contextMenu = new ContextMenu();

        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem viewMenuItem = new MenuItem("View");
        MenuItem deleteMenuItem = new MenuItem("Delete");

        editMenuItem.setOnAction(event -> {
            editUser(selectedUser);
            contextMenu.hide();
        });

        viewMenuItem.setOnAction(event -> {
            viewUser(selectedUser);
            contextMenu.hide();
        });

        deleteMenuItem.setOnAction(event -> {
            deleteUser(selectedUser);
            contextMenu.hide();
        });

        contextMenu.getItems().addAll(editMenuItem, viewMenuItem, deleteMenuItem);

        contextMenu.setOnHidden(e -> {
            // Clean up or do additional actions if needed
        });

        return contextMenu;
    }

    private void editUser(User user) {
        if (user != null) {
            Stage stage = new Stage();
            Parent root = null;
            FXMLLoader loader;

            try {
                loader = new FXMLLoader(UserFormController.class.getResource("user-form.fxml"));
                root = loader.load();

                UserFormController userFormController = loader.getController();
                userFormController.setOnSubmitCallback(submittedUser -> {
                    stage.close();
                    refreshTableView();
                });

                userFormController.initData(user.getIdUser());

                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Utilisateur");

                stage.setResizable(false);
                try (InputStream stream = MainApplication.class.getResourceAsStream("assets/icons/shield-user.png")) {
                    Image icon = new Image(stream);
                    stage.getIcons().add(icon);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(usersTableView.getScene().getWindow());
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void viewUser(User user) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(UserDetailsController.class.getResource("user-details.fxml")));

            root = loader.load();

            // Access the controller and set the Voyage object
            UserDetailsController controller = loader.getController();
            controller.initUser(user);


            stage.setScene(new Scene(root));
            stage.setTitle("User #" + user.getIdUser() + " details");

            // Disable maximize button and resizing
            stage.setResizable(false);


            // Load and set the window icon
            Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("assets/icons/shield-user.png")));
            stage.getIcons().add(icon);

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    usersTableView.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteUser(User user) {
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                userService.remove(user.getIdUser());
                refreshTableView();
            }
        }
    }

    public void refreshTableView() {
        User currentUser = AuthenticationService.getLoggedInUser();
        Role userRole = AuthenticationService.getUserRole();

        if (currentUser.getEntreprise() != null && userRole.equals(Role.COORDINATOR)) {
            allUsers.setAll(userService.getByEntrepriseId(currentUser.getEntreprise().getIdEntreprise()));
        } else if (userRole.equals(Role.ADMIN)) {
            allUsers.setAll(userService.getAll());
        } else {
            allUsers.clear();
        }

        usersTableView.refresh();
    }

    public void handleAddUser(ActionEvent event) {
        Stage stage = new Stage();
        Parent root = null;
        FXMLLoader loader = null;

        try {
            loader = new FXMLLoader(UserFormController.class.getResource("user-form.fxml"));
            root = loader.load();

            UserFormController userFormController = loader.getController();
            userFormController.setOnSubmitCallback(submittedUser -> {
                stage.close();
                refreshTableView();
            });

            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter Utilisateur");

            stage.setResizable(false);
            try (InputStream stream = MainApplication.class.getResourceAsStream("assets/icons/shield-user.png")) {
                Image icon = new Image(stream);
                stage.getIcons().add(icon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleModifier(ActionEvent event) {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            editUser(selectedUser);
        }
    }

    private void filterUsers(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            usersTableView.setItems(allUsers);
            return;
        }

        String[] keywords = searchText.toLowerCase().split("\\s+");

        List<User> filteredUsers = allUsers.stream()
                .filter(user -> Arrays.stream(keywords).allMatch(keyword ->
                        user.getNom().toLowerCase().contains(keyword) ||
                                user.getPrenom().toLowerCase().contains(keyword) ||
                                user.getEmail().toLowerCase().contains(keyword) ||
                                user.getTelephone().toLowerCase().contains(keyword)
                ))
                .collect(Collectors.toList());

        usersTableView.setItems(FXCollections.observableList(filteredUsers));
    }

    private void openUserDetails( User user) {
        viewUser(user);
    }

}
