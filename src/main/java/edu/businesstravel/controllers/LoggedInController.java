package edu.businesstravel.controllers;

import edu.businesstravel.HelloApplication;
import edu.businesstravel.dao.repository.LoginRepository;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoggedInController {
    @FXML
    private PasswordField password;
    @FXML
    private TextField email;
    @FXML
    private Button button;
    private Long idUserConnected;
    private Stage primaryStage;
    private LoginRepository loginRepository;

    public LoggedInController(){}

    public LoggedInController(LoginRepository loginRepository){
        this.loginRepository=loginRepository;
    }

    @FXML
    void login(ActionEvent event) throws SQLException {
        String username = email.getText();
        String pswd = password.getText();

        if (loginRepository.loginUser(event,username,pswd)){

            System.out.println("Login successful");
            idUserConnected = loginRepository.getIdConnected();
            System.out.println(idUserConnected);



        }else{
            System.out.println("Login failed");
        }

    }

//    private void switchToNewScene() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-profile.fxml"));
//
//            Parent root = loader.load();
//
//            // Get the controller of the new scene
//            editProfile newSceneController = loader.getController();
//
//            // You can pass data to the new scene controller if needed
//            // newSceneController.setData(...);
//
//            Scene newScene = new Scene(root);
//
//            // Set the new scene to the primary stage
//            primaryStage.setScene(newScene);
//
//            // Show the primary stage with the new scene
//            primaryStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }






}
