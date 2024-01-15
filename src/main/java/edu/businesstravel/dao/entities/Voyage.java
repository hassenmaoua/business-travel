package edu.businesstravel.dao.entities;

import java.sql.Date;

public class Voyage {
    private long idVoyage;
    private String nom;
    private String depart;
    private String destination;
    private Date dateDepart;
    private String classe;
    private String avion;

    public Voyage(){

    }

    public Voyage( String nom, String depart, String destination, Date dateDepart, String classe, String avion) {
        this.nom = nom;
        this.depart = depart;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.classe = classe;
        this.avion = avion;
    }

    public long getIdVoyage() {
        return idVoyage;
    }

    public void setIdVoyage(long idVoyage) {
        this.idVoyage = idVoyage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getAvion() {
        return avion;
    }

    public void setAvion(String avion) {
        this.avion = avion;
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "idVoyage=" + idVoyage +
                ", nom='" + nom + '\'' +
                ", depart='" + depart + '\'' +
                ", destination='" + destination + '\'' +
                ", dateDepart='" + dateDepart + '\'' +
                ", classe='" + classe + '\'' +
                ", avion='" + avion + '\'' +
                '}';
    }
}
