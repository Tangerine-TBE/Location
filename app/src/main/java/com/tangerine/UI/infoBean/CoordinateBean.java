package com.tangerine.UI.infoBean;

public class CoordinateBean {
    private double longitude;

    private double latitude;

    private boolean isChina;

    public CoordinateBean() {

    }

    public CoordinateBean(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isChina() {
        return isChina;
    }

    public void setChina(boolean china) {
        this.isChina = china;
    }

    @Override
    public String toString(){
        return this.latitude + "_" + this.longitude;
    }

}

