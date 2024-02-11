package edu.businesstravel.tools;

import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class Helpers {

    public static Integer calculateAge(Date date) {
        if (date == null) {
            return null;  // Ou gérez le cas selon vos besoins
        }

        // convertir java.sql.Date en LocalDate
        LocalDate dateDeNaissance = date.toLocalDate();

        // obtenir la date actuelle
        LocalDate dateActuelle = LocalDate.now();

        // calculer la période entre les deux dates
        Period periode = Period.between(dateDeNaissance, dateActuelle);

        // retourner l'age (en années)
        return periode.getYears();
    }

    public static boolean minLength(Control argControl, short argLength) {
        if (argControl instanceof TextField) {
            String text = ((TextField) argControl).getText().trim();
            return text.length() >= argLength;
        }

        if (argControl instanceof TextArea) {
            String text = ((TextArea) argControl).getText().trim();
            return text.length() >= argLength;
        }

        return true;
    }
}
