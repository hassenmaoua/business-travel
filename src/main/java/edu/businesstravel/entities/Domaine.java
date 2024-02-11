package edu.businesstravel.entities;

public enum Domaine {
    IT("Technologies de l'Information"),
    FINANCE("Finance"),
    HEALTHCARE("Santé"),
    EDUCATION("Éducation"),
    MANUFACTURING("Fabrication"),
    RETAIL("Vente au détail"),
    TELECOMMUNICATIONS("Télécommunications"),
    ENERGY("Énergie"),
    TRANSPORTATION("Transport"),
    ENTERTAINMENT("Divertissement"),
    CONSULTING("Conseil"),
    AUTOMOTIVE("Automobile"),
    MEDIA("Médias"),
    REAL_ESTATE("Immobilier"),
    PHARMACEUTICAL("Pharmaceutique"),
    SPORTS("Sports");

    private final String domaine;

    Domaine(String domaine) {
        this.domaine = domaine;
    }

    public String getNom() {
        return domaine;
    }
}
