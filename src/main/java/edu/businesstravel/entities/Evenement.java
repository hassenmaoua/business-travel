package edu.businesstravel.entities;

import java.sql.Date;

public class Evenement {


    private Long idEvenement;
    private String titre;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String region;
    private String adresse;
    private Etat etat;
    private Category categorie;
    private Date dateC;
    private Date dateU;

    public Evenement() {
    }

    public Evenement(String titre, String description, Date dateDebut, Date dateFin, String region, String adresse, Etat etat) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.region = region;
        this.adresse = adresse;
        this.etat = etat;
    }

    public Evenement(String titre, String description, Date dateDebut, Date dateFin, String region, String adresse, Etat etat, Category categorie) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.region = region;
        this.adresse = adresse;
        this.etat = etat;
        this.categorie = categorie;
    }

    public Evenement(Long idEvenement, String titre, String description, Date dateDebut, Date dateFin, String region, String adresse, Etat etat, Category categorie, Date dateC, Date dateU) {
        this.idEvenement = idEvenement;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.region = region;
        this.adresse = adresse;
        this.etat = etat;
        this.categorie = categorie;
        this.dateC = dateC;
        this.dateU = dateU;
    }

    public Long getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(Long idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Category getCategorie() {
        return categorie;
    }

    public void setCategorie(Category categorie) {
        this.categorie = categorie;
    }

    public Date getDateC() {
        return dateC;
    }

    public void setDateC(Date dateC) {
        this.dateC = dateC;
    }

    public Date getDateU() {
        return dateU;
    }

    public void setDateU(Date dateU) {
        this.dateU = dateU;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "idEvenement=" + idEvenement +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", region='" + region + '\'' +
                ", adresse='" + adresse + '\'' +
                ", etat=" + etat +
                ", Categorie=" + categorie +
                ", dateC=" + dateC +
                ", dateU=" + dateU +
                '}';
    }
}
