package com.google.sunnyday.service.model;

public class City {
    private String country;

    private Coord coord;

    private String name;

    private String id;

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public Coord getCoord ()
    {
        return coord;
    }

    public void setCoord (Coord coord)
    {
        this.coord = coord;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [country = "+country+", coord = "+coord+", name = "+name+", id = "+id+"]";
    }
}
