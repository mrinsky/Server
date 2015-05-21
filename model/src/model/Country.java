package model;

import java.io.Serializable;

public class Country implements Serializable, Comparable<Country> {
    private String name;

    public Country(String name) {
        this.name = name;}

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

    @Override
    public int compareTo(Country country) {
        if (this.getName().charAt(0) > country.getName().charAt(0)) return 1;
        else if (this.getName().charAt(0) < country.getName().charAt(0)) return -1;
        return 0;
    }
}
