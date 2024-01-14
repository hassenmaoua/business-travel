package edu.businesstravel.dao.entities;

import java.util.List;

public class Category {
    private  Long idCategory;
    private String name;
     private List<Event> eventList;
    public Long getIdCategory() {
        return idCategory;
    }

    public Category(Long idCategory, String name, List<Event> eventList) {
        this.idCategory = idCategory;
        this.name = name;
        this.eventList = eventList;
    }
    public Category(){}

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", name='" + name + '\'' +
                ", eventList=" + eventList +
                '}';
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
