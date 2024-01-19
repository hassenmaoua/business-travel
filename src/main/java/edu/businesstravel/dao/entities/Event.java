package edu.businesstravel.dao.entities;

import java.sql.Date;

public class Event {

    private Long idEvent;
    private String title;

    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String region;
    private String adresse;
    private Etat status;
    private Long category;

    public Event(Long idEvent, String title, String description, Date dateDebut, Date dateFin, String region, String adresse, Etat status, Long category) {
        this.idEvent = idEvent;
        this.title = title;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.region = region;
        this.adresse = adresse;
        this.status = status;
        this.category = category;
    }
    public Event(){

    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Etat getStatus() {
        return status;
    }

    public void setStatus(Etat status) {
        this.status = status;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent=" + idEvent +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", region='" + region + '\'' +
                ", adresse='" + adresse + '\'' +
                ", status=" + status +
                ", category=" + category +
                '}';
    }
}
