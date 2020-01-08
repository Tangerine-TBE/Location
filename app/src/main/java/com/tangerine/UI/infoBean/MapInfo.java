package com.tangerine.UI.infoBean;

public class MapInfo {
    public MapInfo(double x,double y,String address ,String country,String province){
        this.x = x;
        this.y = y;
        this.address = address;
        this.country = country;
        this.province = province;
    }
    public double x;
    public double y;
    public String address;//详细地址
    public String country;
    public String province;
}
