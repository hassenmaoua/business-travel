package edu.businesstravel.client.category;


import edu.businesstravel.entities.Category;
import edu.businesstravel.repository.category.CategoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    private Button modifierButton;

    @FXML
    private Button supprimerButton;
    @FXML
    private TextField newCategoryNameTextField;
    @FXML
    private Button saveButton;
    @FXML
    private void ajoutCategory() {


        String categoryName = categoryNameTextField.getText();
        System.out.println("Catégorie ajoutée : " + categoryName);
        Category c=new Category(categoryName);
        categoryRepository.save(c);
        categoryNameTextField.clear();
        refreshTable();



    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       idCategory.setCellValueFactory(new PropertyValueFactory<>("idCategory"));
        nomCategory.setCellValueFactory(new PropertyValueFactory<>("name"));

        try {
            List<Category> categorys = categoryRepository.findAll();
            ObservableList<Object> allCategory = FXCollections.observableArrayList(categorys);

            // Définissez l'ObservableList comme modèle de données de la TableView
            categoryTable.setItems(allCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void supprimerCategory() {
        // TODO : Implémenter la logique de suppression d'une catégorie
        // Récupérer la catégorie sélectionnée
        Category selectedCategory = (Category) categoryTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Confirmer la suppression
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Suppression d'une catégorie");
            alert.setHeaderText("Confirmation de suppression");
            alert.setContentText("Voulez-vous vraiment supprimer la catégorie " + selectedCategory.getName() + " ?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                // Supprimer la catégorie
                // TODO : Implémenter la suppression de la catégorie dans la base de données
              //  categoryTable.getItems().remove(selectedCategory);
                categoryRepository.deleteOne(selectedCategory.getIdCategory());
             //   categoryTable.refresh();
                refreshTable();
            }
        } else {
            // Aucune catégorie sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune catégorie sélectionnée");
            alert.setContentText("Veuillez sélectionner une catégorie à supprimer.");
            alert.showAndWait();
        }
    }


    @FXML
    private void modifierCategory(ActionEvent event) throws IOException {
        // Récupérer la catégorie sélectionnée
        Category selectedCategory = (Category) categoryTable.getSelectionModel().getSelectedItem();

        // Charger le fichier FXML pour la boîte de dialogue
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/businesstravel/modif_category.fxml"));
        Parent root = loader.load();

        // Obtenir le contrôleur de la boîte de dialogue
        CategoryController dialogController = loader.getController();

        // Configurer le contrôleur de la boîte de dialogue avec la catégorie sélectionnée
     //   dialogController.setCategory(selectedCategory);
categoryRepository.update(dialogController.nomCategory,selectedCategory.getIdCategory());
        // Créer une boîte de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().setContent(root);

        // Ajouter les boutons OK et Annuler à la boîte de dialogue
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = dialog.showAndWait();

        // Vérifier si l'utilisateur a cliqué sur OK et mettre à jour la catégorie si nécessaire
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Mettre à jour la catégorie (utiliser la logique de mise à jour de votre contrôleur de boîte de dialogue)

            // Mettez à jour la catégorie avec les nouvelles informations
            selectedCategory.setName(newCategoryNameTextField.getText()); // Assurez-vous que vous avez un setter approprié dans votre classe Category

            // Fermer la boîte de dialogue
            closeDialog();
            // Rafraîchir la table si nécessaire
            refreshTable();
        }
    }
    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }


    private void refreshTable() {
        try {
            List<Category> categorys = categoryRepository.findAll();
            ObservableList<Object> allcats = FXCollections.observableArrayList(categorys);

            categoryTable.setItems(allcats);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
