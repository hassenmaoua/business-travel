package edu.businesstravel.controllers;

import edu.businesstravel.dao.entities.Role;
import edu.businesstravel.dao.entities.User;
import edu.businesstravel.dao.repository.UserRepository;
import edu.businesstravel.dao.tools.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class UserSelectionController implements Initializable {

    @FXML
    private Button addUserBtn;

    @FXML
    private TableColumn<User, Long> idColumn;

    @FXML
    private TableColumn<User, String> nomColumn;

    @FXML
    private TableColumn<User, String> prenomColumn;

    @FXML
    private TableColumn<User, Role> roleColumn;

    @FXML
    private Button searchUserBtn;

    @FXML
    private TextField searchUserField;

    @FXML
    private TableView<User> usersTableView;

    ObservableList<User> initialData() {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        UserRepository userRepository = new UserRepository(connection);
        List<User> users = (List<User>) userRepository.findAll();

        return FXCollections.<User>observableList(users);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<User, Long>("idUser"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<User, String>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<User, String>("prenom"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<User, Role>("role"));

        usersTableView.setItems(initialData());
    }
}
