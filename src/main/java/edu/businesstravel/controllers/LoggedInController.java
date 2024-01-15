package edu.businesstravel.controllers;

import edu.businesstravel.dao.repository.LoginRepository;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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



        }else{
            System.out.println("Login failed");
        }
    }




}
