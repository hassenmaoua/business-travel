package edu.businesstravel.dao.entities;

public class Entreprise {
    private long idEntreprise;
    private String raisonSociale;
    private String adresse;
    private Domaine domaine;

    public Entreprise() {
    }

    public Entreprise(String raisonSociale, String adresse) {
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
        this.domaine = new Domaine();
    }

    public Entreprise(String raisonSociale, String adresse, Domaine domaine) {
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
        this.domaine = domaine;
    }

    public long getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(long idEntreprise) {
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

    @Override
    public String toString() {
        return "Entreprise{" +
                "idEntreprise=" + idEntreprise +
                ", raisonSociale='" + raisonSociale + '\'' +
                ", adresse='" + adresse + '\'' +
                ", domaine=" + domaine +
                '}';
    }
}
