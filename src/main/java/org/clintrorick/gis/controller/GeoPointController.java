package org.clintrorick.gis.controller;

import org.clintrorick.gis.domain.CityDistance;
import org.clintrorick.gis.domain.CoordinateExistence;
import org.clintrorick.gis.domain.FinalAnswer;
import org.clintrorick.gis.domain.GeoPoint;
import org.clintrorick.gis.mapper.GeoPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("geopoints")
public class GeoPointController {

    @Autowired
    private GeoPointMapper geoPointMapper;

    @GetMapping("/all")
    public List<GeoPoint> getAllDataSets(){
        return geoPointMapper.findAll();
    }

    private static final String COMMA = ",";

    @GetMapping("/citydistance")
    public List<CityDistance> cityDistance(@RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude) {
        String wktCoordsRep = String.format("POINT(%s %s)", longitude, latitude);// WKT = Well-Known Text, a geo text format


        List<CityDistance> cityDistancesInMeters = geoPointMapper.distanceFromCities(wktCoordsRep);

        return convertCityDistancesToMiles(cityDistancesInMeters);
    }

    private List<CityDistance> convertCityDistancesToMiles(List<CityDistance> cityDistancesInMeters){
        return cityDistancesInMeters.stream().map((citydistance)->{
            CityDistance cityDistanceNew = new CityDistance();//avoid mutating
            cityDistanceNew.setCityName(citydistance.getCityName());
            cityDistanceNew.setDistance(citydistance.getDistance().multiply(new BigDecimal("0.0006213712")));
            return cityDistanceNew;
        }).collect(Collectors.toList());
    }

    private String getWellKnownTextRep(BigDecimal longitude, BigDecimal latitude){
       return String.format("POINT(%s %s)", longitude, latitude);// WKT = Well-Known Text, a geo text format

    }


    @RequestMapping(value = "/calculateFullAnswer.csv", produces = "text/csv")
    public void returnCsv(HttpServletResponse response) throws IOException {
        List<FinalAnswer> finalAnswers = new ArrayList<>();
        List<GeoPoint> allGeoPointsInDb = getAllDataSets();//imagine a ReST client here :)
        for (GeoPoint geoPoint : allGeoPointsInDb){
            String wellKnownTextCoordRepresentation = getWellKnownTextRep(geoPoint.getLatitude(), geoPoint.getLongitude());

            boolean isInUnitedStates = isPointInUnitedStates(geoPoint.getLatitude(), geoPoint.getLongitude());

            List<CityDistance> cityDistancesInMiles = convertCityDistancesToMiles(geoPointMapper.distanceFromCities(wellKnownTextCoordRepresentation));
            List<String> citiesWithin500Miles = cityDistancesInMiles.stream()
                    .filter((cityDistance)->new BigDecimal(500).compareTo(cityDistance.getDistance()) > 0)
                    .map(CityDistance::getCityName)
              .collect(Collectors.toList());

            var finalAnswer = new FinalAnswer();

            finalAnswer.setCityNamesWithin500Miles(String.join(";",citiesWithin500Miles));
            finalAnswer.setLatitude(geoPoint.getLatitude());
            finalAnswer.setLongitude(geoPoint.getLongitude());
            finalAnswer.setInUnitedStates(isInUnitedStates);
            finalAnswer.setCityDistances(cityDistancesInMiles);
            finalAnswers.add(finalAnswer);

        }
        response.setContentType("text/csv; charset=utf-8");

        StringBuilder builder = new StringBuilder();
        builder.append("CityNamesWithin500Miles");
        builder.append(COMMA);
        builder.append("Longitude");
        builder.append(COMMA);
        builder.append("Latitude");
        builder.append(COMMA);
        builder.append("InUnitedStates");
        builder.append(COMMA);
        builder.append("Tokyo_distance_miles");
        builder.append(COMMA);
        builder.append("Sydney_distance_miles");
        builder.append(COMMA);
        builder.append("Riyadh_distance_miles");
        builder.append(COMMA);
        builder.append("Zurich_distance_miles");
        builder.append(COMMA);
        builder.append("Reykvjavik_distance_miles");
        builder.append(COMMA);
        builder.append("MexicoCity_distance_miles");
        builder.append(COMMA);
        builder.append("Lima_distance_miles");
        builder.append("\n");
        String[] CITIES = new String[]{"Tokyo","Sydney","Riyadh","Zurich","Reykvjavik","Mexico City","Lima"};

        for (FinalAnswer answer : finalAnswers){
            builder.append(answer.getCityNamesWithin500Miles());
            builder.append(COMMA);

            builder.append(answer.getLongitude());
            builder.append(COMMA);

            builder.append(answer.getLatitude());
            builder.append(COMMA);

            builder.append(answer.isInUnitedStates());
            builder.append(COMMA);

            for (String cityName : CITIES){
                var distance = answer
                        .getCityDistances().stream().filter(cd -> cd.getCityName().equals(cityName))
                        .findFirst().get().getDistance();
                builder.append(distance);
                builder.append(COMMA);
            }
            builder.append("\n");
        }
        response.getWriter().print(builder.toString());

    }



    @GetMapping("/inunitedstates")
    public boolean isPointInUnitedStates(@RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude) {

        String wktCoordsRep = String.format("POINT(%s %s)", longitude, latitude);// WKT = Well-Known Text, a geo text format
        var isPointInUnitedStates = geoPointMapper.isPointInUnitedStates(wktCoordsRep);
        return isPointInUnitedStates != null;
    }

    // ASSUMPTION: Latitude and Longitude are provided with at least 6-digit scale. e.g. -42.123456
    // This API will accept lat/long of less scale, but will return a "best guess" of whether the point exists in the DB.
    // Equality is determined using 6-digit scale, with both user inputs and points stored in the database truncated to 6 digits of scale.
    // e.g. -42.12345678 will be truncated to -42.123456 before comparing for equality.

    @GetMapping("")
    public CoordinateExistence getData(@RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude){
        if (latitude.compareTo(new BigDecimal(-90)) < 0 || latitude.compareTo(new BigDecimal(90)) > 0){
            var coordExistence = new CoordinateExistence();
            coordExistence.setHasErrors(true);
            coordExistence.setErrorMessage(
                    String.format( "Latitude must be between -90 and 90. Provided: %s", latitude) );
            return coordExistence;
        }
        if (longitude.compareTo(new BigDecimal(-180)) < 0 || longitude.compareTo(new BigDecimal(180)) > 0){
            var coordExistence = new CoordinateExistence();
            coordExistence.setHasErrors(true);
            coordExistence.setErrorMessage(
                    String.format( "Longitude must be between -180 and 180. Provided: %s", longitude) );
            return coordExistence;
        }
        var geoPoint = geoPointMapper.findGeoPoint(latitude, longitude);

        if (geoPoint!=null){
            var coordExists = new CoordinateExistence();
            coordExists.setCoordinateExistsInDb(true);
            return coordExists;
        }

        var coordDoesNotExist = new CoordinateExistence();
        coordDoesNotExist.setCoordinateExistsInDb(false);
        return coordDoesNotExist;
    }
}
