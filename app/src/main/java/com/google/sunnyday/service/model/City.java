package com.google.sunnyday.service.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.sunnyday.R;

@Entity(tableName = "city_table")
public class City {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    public City(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return this.name;
    }
    @NonNull
    public void setName(@NonNull String cityName) {
        this.name = cityName;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    //    private String country;
//    private Coord coord;
//    private String name;
//    private String id;
//
//    public String getCountry ()
//    {
//        return country;
//    }
//    public void setCountry (String country)
//    {
//        this.country = country;
//    }
//    public Coord getCoord ()
//    {
//        return coord;
//    }
//    public void setCoord (Coord coord)
//    {
//        this.coord = coord;
//    }
//    public String getName ()
//    {
//        return name;
//    }
//    public void setName (String name)
//    {
//        this.name = name;
//    }
//    public String getId ()
//    {
//        return id;
//    }
//    public void setId (String id)
//    {
//        this.id = id;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "ClassCity [country = "+country+", coord = "+coord+", name = "+name+", id = "+id+"]";
//    }


}
