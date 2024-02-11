package edu.businesstravel.entities;

import java.sql.Date;

public class Companion {
    private long idCompanion;
    private Evenement evenement;
    private User employee;
    private Voyage voyage;
    private String nom;
    private String domaineActivite;
    private Integer age;
    private String besoinsSpeciaux;
    private String contactUrgenceNom;
    private String contactUrgenceTel;
    private String restrictionsAlimentaires;
    private String numPassport;
    private String notesSupplementaires;
    private Date dateC;
    private Date dateU;

    public Companion(){}


    public Companion(Evenement evenement, User employee, Voyage voyage, String nom, String domaineActivite, Integer age, String besoinsSpeciaux, String contactUrgenceNom, String contactUrgenceTel, String restrictionsAlimentaires, String numPassport, String nationalite, String notesSupplementaires) {
        this.evenement = evenement;
        this.employee = employee;
        this.voyage = voyage;
        this.nom = nom;
        this.domaineActivite = domaineActivite;
        this.age = age;
        this.besoinsSpeciaux = besoinsSpeciaux;
        this.contactUrgenceNom = contactUrgenceNom;
        this.contactUrgenceTel = contactUrgenceTel;
        this.restrictionsAlimentaires = restrictionsAlimentaires;
        this.numPassport = numPassport;
        this.notesSupplementaires = notesSupplementaires;
    }

    public Companion(long idCompanion, Evenement evenement, User employee, Voyage voyage, String nom, String domaineActivite, Integer age, String besoinsSpeciaux, String contactUrgenceNom, String contactUrgenceTel, String restrictionsAlimentaires, String numPassport, String nationalite, String notesSupplementaires, Date dateC, Date dateU) {
        this.idCompanion = idCompanion;
        this.evenement = evenement;
        this.employee = employee;
        this.voyage = voyage;
        this.nom = nom;
        this.domaineActivite = domaineActivite;
        this.age = age;
        this.besoinsSpeciaux = besoinsSpeciaux;
        this.contactUrgenceNom = contactUrgenceNom;
        this.contactUrgenceTel = contactUrgenceTel;
        this.restrictionsAlimentaires = restrictionsAlimentaires;
        this.numPassport = numPassport;
        this.notesSupplementaires = notesSupplementaires;
        this.dateC = dateC;
        this.dateU = dateU;
    }

    public long getIdCompanion() {
        return idCompanion;
    }

    public void setIdCompanion(long idCompanion) {
        this.idCompanion = idCompanion;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDomaineActivite() {
        return domaineActivite;
    }

    public void setDomaineActivite(String domaineActivite) {
        this.domaineActivite = domaineActivite;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBesoinsSpeciaux() {
        return besoinsSpeciaux;
    }

    public void setBesoinsSpeciaux(String besoinsSpeciaux) {
        this.besoinsSpeciaux = besoinsSpeciaux;
    }


    public String getContactUrgenceNom() {
        return contactUrgenceNom;
    }

    public void setContactUrgenceNom(String contactUrgenceNom) {
        this.contactUrgenceNom = contactUrgenceNom;
    }

    public String getContactUrgenceTel() {
        return contactUrgenceTel;
    }

    public void setContactUrgenceTel(String contactUrgenceTel) {
        this.contactUrgenceTel = contactUrgenceTel;
    }

    public String getRestrictionsAlimentaires() {
        return restrictionsAlimentaires;
    }

    public void setRestrictionsAlimentaires(String restrictionsAlimentaires) {
        this.restrictionsAlimentaires = restrictionsAlimentaires;
    }

    public String getNumPassport() {
        return numPassport;
    }

    public void setNumPassport(String numPassport) {
        this.numPassport = numPassport;
    }

    public String getNotesSupplementaires() {
        return notesSupplementaires;
    }

    public void setNotesSupplementaires(String notesSupplementaires) {
        this.notesSupplementaires = notesSupplementaires;
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
        return "Companion{" +
                "idCompanion=" + idCompanion +
                ", evenement=" + evenement +
                ", employee=" + employee +
                ", voyage=" + voyage +
                ", nom='" + nom + '\'' +
                ", domaineActivite='" + domaineActivite + '\'' +
                ", age=" + age +
                ", besoinsSpeciaux='" + besoinsSpeciaux + '\'' +
                ", contactUrgenceNom='" + contactUrgenceNom + '\'' +
                ", contactUrgenceTel='" + contactUrgenceTel + '\'' +
                ", restrictionsAlimentaires='" + restrictionsAlimentaires + '\'' +
                ", numPassport='" + numPassport + '\'' +
                ", notesSupplementaires='" + notesSupplementaires + '\'' +
                ", dateC=" + dateC +
                ", dateU=" + dateU +
                '}';
    }
}
