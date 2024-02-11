package edu.businesstravel.entities;

public enum Etat {
    NOUVEAUX("Nouveaux"),
    EN_COURS("En Cours"),
    TERMINER("Terminer"),
    ANNULER("Annuler");

    private String displayEtat;

    Etat(String displayEtat) {
        this.displayEtat = displayEtat;
    }

    public String displayEtat() {
        return displayEtat;
    }

    @Override
    public String toString() {
        return displayEtat;
    }
}
