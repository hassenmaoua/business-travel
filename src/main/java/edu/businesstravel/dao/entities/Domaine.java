package edu.businesstravel.dao.entities;

public class Domaine {
    private long idDomaine;
    private String nom;

    public Domaine() {
        super();
    }
    public Domaine(long idDomaine, String nom) {
        this.idDomaine = idDomaine;
        this.nom = nom;
    }

    public long getIdDomaine() {
        return idDomaine;
    }

    public void setIdDomaine(long idDomaine) {
        this.idDomaine = idDomaine;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Domaine{" +
                "idDomaine=" + idDomaine +
                ", nom='" + nom + '\'' +
                '}';
    }
}
