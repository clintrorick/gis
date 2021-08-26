package org.clintrorick.gis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.clintrorick.gis.domain.CityDistance;
import org.clintrorick.gis.domain.GeoPoint;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GeoPointMapper {

    List<GeoPoint> findAll();

    GeoPoint findGeoPoint(BigDecimal latitude, BigDecimal longitude);

    Boolean isPointInUnitedStates(String wktCoordsRep);

    List<CityDistance> distanceFromCities(String wktCoords);
}
