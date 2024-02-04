package edu.businesstravel.dao.controller;

import edu.businesstravel.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SideBarController implements Initializable {
    @FXML
    private BorderPane bp;
    public void Categories(javafx.scene.input.MouseEvent mouseEvent) {
        loadPage("/edu/businesstravel/add_category");
    }

    public void Eevenement(javafx.scene.input.MouseEvent mouseEvent) {
        loadPage("/edu/businesstravel/list_event");
    }
    public void ajoutEvenement(javafx.scene.input.MouseEvent mouseEvent) {
        loadPage("/edu/businesstravel/add_event");
    }
    public void loadPage(String page) {
        Parent root  = null;

        try {
      //      FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add_category.fxml"));

            root= FXMLLoader.load(getClass().getResource(page+".fxml"));

        }
        catch (IOException ex) {
            Logger.getLogger(SideBarController.class.getName()).log(Level.SEVERE,null,ex);

        }
        bp.setCenter(root);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
