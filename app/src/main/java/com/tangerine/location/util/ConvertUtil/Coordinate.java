package com.tangerine.location.util.ConvertUtil;

import java.math.BigDecimal;

public class Coordinate {
    public BigDecimal lat;
    public BigDecimal lon;

    public Coordinate(BigDecimal lat, BigDecimal lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }
}
