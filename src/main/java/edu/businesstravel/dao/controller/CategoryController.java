package edu.businesstravel.dao.controller;

import edu.businesstravel.dao.entities.Category;
import edu.businesstravel.dao.repository.CategoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {
    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TableColumn<Category, Long> idCategory;
    @FXML
    private TableColumn<Category, String> nomCategory;
    @FXML
    private TableView<Object> categoryTable;
    CategoryRepository categoryRepository=new CategoryRepository();

    @FXML
    private void ajoutCategorie() {
        String categoryName = categoryNameTextField.getText();
        System.out.println("Catégorie ajoutée : " + categoryName);

        categoryNameTextField.clear();



    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCategory.setCellValueFactory(new PropertyValueFactory<>("idCategory"));
        nomCategory.setCellValueFactory(new PropertyValueFactory<>("name"));

        try {
            List<Object> categories = categoryRepository.findAll();
            ObservableList<Object> allCategory = FXCollections.observableArrayList(categories);

            // Définissez l'ObservableList comme modèle de données de la TableView
            categoryTable.setItems(allCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
