package edu.businesstravel;

import edu.businesstravel.controllers.LoggedInController;
import edu.businesstravel.dao.repository.LoginRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600,400);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(true);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}