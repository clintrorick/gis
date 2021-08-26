package org.clintrorick.gis.domain;

import java.math.BigDecimal;

public class CityDistance {
    private String cityName;
    private BigDecimal distance;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }
}
