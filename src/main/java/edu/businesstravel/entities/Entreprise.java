package edu.businesstravel.entities;

import java.sql.Date;

public class Entreprise {
    private Long idEntreprise;
    private String nomEntreprise;
    private String raisonSociale;
    private String adresse;
    private Domaine domaine;
    private String email;
    private String telephone;
    private Long nbEmployee;
    private Date dateC;
    private Date dateU;

    public Entreprise() {
    }

    public Entreprise(String raisonSociale, String adresse) {
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
    }

    public Entreprise(String raisonSociale, String adresse, Domaine domaine) {
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
        this.domaine = domaine;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setNbEmployee(Long nbEmployee) {
        this.nbEmployee = nbEmployee;
    }

    public String getNomEntreprise() {
        return nomEntreprise;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Long getNbEmployee() {
        return nbEmployee;
    }

    public Long getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(Long idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public Date getDateC() {
        return dateC;
    }

    public void setDateC(Date dateC) {

        this.dateC = new Date(System.currentTimeMillis());
    }

    public Date getDateU() {
        return dateU;
    }

    public void setDateU(Date dateU) {

        this.dateU = new Date(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Entreprise{" +

                "idEntreprise=" + idEntreprise +
                "nomEntreprise="+nomEntreprise+
                ", raisonSociale='" + raisonSociale + '\'' +
                ", adresse='" + adresse + '\'' +
                ", domaine=" + domaine +
                ", email=" + email +
                ", tel=" + telephone +
                ", nombre d'employee=" + nbEmployee +
                ", dateC=" + dateC +
                ", dateU=" + dateU +
                '}';
    }

}
