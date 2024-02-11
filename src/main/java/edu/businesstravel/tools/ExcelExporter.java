package edu.businesstravel.tools;

import edu.businesstravel.entities.Domaine;
import edu.businesstravel.entities.Entreprise;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {
    public static void exportToExcel(List<Entreprise> entreprises, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Entreprises");

            // Créez une ligne pour les en-têtes
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nom Entreprise");
            headerRow.createCell(1).setCellValue("Raison Sociale");
            headerRow.createCell(2).setCellValue("Adresse");
            headerRow.createCell(3).setCellValue("Secteur d'Activité");
            headerRow.createCell(4).setCellValue("Email Entreprise");
            headerRow.createCell(5).setCellValue("Telephone");
            headerRow.createCell(6).setCellValue("Nombre d'employée");

            // Remplissez les données dans les lignes suivantes
            int rowNum = 1;
            for (Entreprise entreprise : entreprises) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entreprise.getNomEntreprise());
                row.createCell(1).setCellValue(entreprise.getRaisonSociale());
                row.createCell(2).setCellValue(entreprise.getAdresse());
                Domaine domaine = entreprise.getDomaine();
                String secteurActivite = (domaine != null) ? domaine.getNom() : "Non spécifié";
                row.createCell(3).setCellValue(secteurActivite);
                row.createCell(4).setCellValue(entreprise.getEmail());
                row.createCell(5).setCellValue(entreprise.getTelephone());
                row.createCell(6).setCellValue(entreprise.getNbEmployee());
            }

            // Écrivez le contenu dans le fichier Excel
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Exportation vers Excel réussie !");
        } catch (Exception e) {
            e.printStackTrace();
            // Gérez toute exception liée à l'exportation vers Excel
            // Affichez un message d'erreur à l'utilisateur si nécessaire
        }
    }

}
