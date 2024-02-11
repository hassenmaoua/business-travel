package edu.businesstravel.entities;

public class Category {
    private  Long idCategory;
    private String name;



    public Long getIdCategory() {
        return idCategory;
    }

    public Category(Long idCategory, String name) {
        this.idCategory = idCategory;
        this.name = name;

    }
    public Category(){}

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", name='" + name + '\'' +

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

    public Category(String name) {
        this.name = name;
    }
}
