package edu.businesstravel.dao.entities;

import java.sql.Date;

public class User {
    private long idUser;
    private String email;
    private String pswd;
    private Role role;
    private String nom;
    private String prenom;
    private String adresse;
    private Date dateNaissance;
    private String telephone;
    private Entreprise entreprise;

    public User() {}
    public User(String email, String pswd, Role role, String nom, String prenom, String adresse, Date dateNaissance, String telephone) {
        this.email = email;
        this.pswd = pswd;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
    }

    public User(String email, String pswd, Role role, String nom, String prenom, String adresse, Date dateNaissance, String telephone, Entreprise entreprise) {
        this.email = email;
        this.pswd = pswd;
        this.role = role;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.entreprise = entreprise;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", email='" + email + '\'' +
                ", pswd='" + pswd + '\'' +
                ", role=" + role +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", telephone='" + telephone + '\'' +
                ", entreprise=" + entreprise +
                '}';
    }
}
