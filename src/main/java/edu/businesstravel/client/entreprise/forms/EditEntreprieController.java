package edu.businesstravel.client.entreprise.forms;

import edu.businesstravel.entities.Entreprise;
import edu.businesstravel.repository.EntrepriseRepository;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditEntreprieController {
    @FXML
    private TextField nomEntrepriseField;
    @FXML
    private TextField raisonSocialeField;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField numeroTelephoneField;
    @FXML
    private TextField nbEmployeeField;


    private Entreprise selectedEntreprise;

    private EntrepriseRepository entrepriseRepository;
    public EditEntreprieController() {
        this.entrepriseRepository = new EntrepriseRepository();
    }

    public void loadEnterpriseData(Entreprise entreprise) {
        selectedEntreprise = entreprise;
        nomEntrepriseField.setText(entreprise.getNomEntreprise());
        raisonSocialeField.setText(entreprise.getRaisonSociale());
        adresseField.setText(entreprise.getAdresse());
        emailField.setText(entreprise.getEmail());
        numeroTelephoneField.setText(entreprise.getTelephone());
        nbEmployeeField.setText(String.valueOf(entreprise.getNbEmployee()));
        //editSecteurActiviteField.setText(entreprise.getSecteurActivite());
    }
    @FXML
    public void editEntreprise(){
        // Retrieve edited data from text fields
        if (selectedEntreprise != null) {
            // Update the fields of the existing entreprise with the new values
            selectedEntreprise.setNomEntreprise(nomEntrepriseField.getText());
            selectedEntreprise.setRaisonSociale(raisonSocialeField.getText().trim());
            selectedEntreprise.setAdresse(adresseField.getText());
            selectedEntreprise.setEmail(emailField.getText());
            selectedEntreprise.setTelephone(numeroTelephoneField.getText());
            selectedEntreprise.setNbEmployee(Long.valueOf(nbEmployeeField.getText()));

            // Use the edit method to update the entreprise in the database
            entrepriseRepository.save(selectedEntreprise);

            // Close the window or perform any other necessary actions
            closeWindow();
        }

    }

    @FXML
    public void closeWindow(){}
}
