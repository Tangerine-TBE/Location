package com.tangerine.UI.infoBean;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.NameInDb;
import io.objectbox.annotation.Unique;

@Entity
public class MapInfo {
    @Id
    long id;
    @Unique
    @Index
    @NameInDb("MapInfo")
    public String address;//详细地址
    public String country;
    public String province;
    public double x;
    public double y;
    public MapInfo(){

    }
    public MapInfo(double x, double y, String address, String country, String province) {
        this.x = x;
        this.y = y;
        this.address = address;
        this.country = country;
        this.province = province;
    }

    public MapInfo(long id, double x, double y, String address, String country, String province) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.address = address;
        this.country = country;
        this.province = province;
    }

}
