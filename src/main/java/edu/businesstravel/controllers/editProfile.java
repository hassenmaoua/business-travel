package edu.businesstravel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class editProfile{
    @FXML
    private Label userIdLabel;
    private Long connectedUserId;
    public void setConnectedUserId(Long connectedUserId) {
        this.connectedUserId = connectedUserId;
        userIdLabel.setText("User ID: " + connectedUserId);
    }
}
