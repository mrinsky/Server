package model;

import java.io.Serializable;

public class Country implements Serializable {
    private String name;

    public Country(String name) {
        this.name = name;}

    public Country() {
        // то есть если вызвался пустой конструктор, то название будет неизбежно null? 
        // Надо хотя бы пустой строкой инициализировать
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (!name.equals(country.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
