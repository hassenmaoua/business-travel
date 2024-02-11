package edu.businesstravel.client.user.details;

import edu.businesstravel.MainApplication;
import edu.businesstravel.entities.Role;
import edu.businesstravel.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserDetailsController implements Initializable {
    private User user;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label companyDomaineLabel;

    @FXML
    private Label companyNameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private ImageView entrepriseImageView;

    @FXML
    private Label roleLabel;

    @FXML
    private Label telephoneLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initUser(User u) {
        this.user = u;

        nomLabel.setText(user.getNom() + " " + user.getPrenom());
        roleLabel.setText(user.getRole().name());

        emailLabel.setText(user.getEmail());
        telephoneLabel.setText(user.getTelephone());
        adresseLabel.setText(user.getAdresse());

        if (user.getRole().equals(Role.ADMIN)) {
            profileImageView.setImage(new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("assets/icons/admin.png"))));
            profileImageView.setScaleX(0.8);
            profileImageView.setScaleY(0.8);
            entrepriseImageView.setVisible(false);
            companyDomaineLabel.setVisible(false);
            companyNameLabel.setVisible(false);
        }
        else {
            entrepriseImageView.setVisible(true);
            companyDomaineLabel.setVisible(true);
            companyNameLabel.setVisible(true);

            if (user.getEntreprise() != null)
            {
                companyNameLabel.setText(user.getEntreprise().getNomEntreprise());
                companyDomaineLabel.setText(user.getEntreprise().getDomaine().getNom());
            }
        }
    }
}
