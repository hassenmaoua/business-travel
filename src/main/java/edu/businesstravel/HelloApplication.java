package edu.businesstravel;
import edu.businesstravel.controllers.LoggedInController;
import edu.businesstravel.dao.repository.LoginRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginRepository loginRepository = new LoginRepository();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        LoggedInController controller = new LoggedInController(loginRepository);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Business Travel App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
