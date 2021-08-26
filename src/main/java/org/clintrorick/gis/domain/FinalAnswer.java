package org.clintrorick.gis.domain;

import java.math.BigDecimal;
import java.util.List;

public class FinalAnswer {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean isInUnitedStates;
    private String cityNamesWithin500Miles;
    private List<CityDistance> cityDistances;

    public List<CityDistance> getCityDistances() {
        return cityDistances;
    }

    public void setCityDistances(List<CityDistance> cityDistances) {
        this.cityDistances = cityDistances;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public boolean isInUnitedStates() {
        return isInUnitedStates;
    }

    public void setInUnitedStates(boolean inUnitedStates) {
        isInUnitedStates = inUnitedStates;
    }

    public String getCityNamesWithin500Miles() {
        return cityNamesWithin500Miles;
    }

    public void setCityNamesWithin500Miles(String cityNamesWithin500Miles) {
        this.cityNamesWithin500Miles = cityNamesWithin500Miles;
    }

}
