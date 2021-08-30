To generate the CSV answer to the solution, issue a GET request to localhost:8080/geopoints/calculateFullAnswer.csv

To seed the database, I started with a shapefile provided by the U.S. census.gov website 
that denotes boundaries of all 50 U.S. states.

https://www.census.gov/geographies/mapping-files/time-series/geo/carto-boundary-file.html

Specifically, I used the Nation-based file cb_2018_us_nation_20m.zip, which can be downloaded from the above link.

Then, I used the site https://mygeodata.cloud to convert the shapefile to WKT (well-known text) format:

The resultant WKT is loaded into the `gis.united_states_polygon` table

To create the 10,000 random lat/long coordinates, I used PostGIS's ST_GeneratePoints function.
https://postgis.net/docs/ST_GeneratePoints.html

ST_GeneratePoints accepts a polygon and returns N points within that polygon.  
I used a polygon which covers most of the inhabited portions of the globe.

The third parameter to ST_GeneratePoints is a seed, meaning the 10,000 points will be generated deterministically, 
making testing more reliable.

The lat/long for the cities to check distance against were acquired via latlong.net.  
I used points rather than polygons to represent the cities for the sake of time.



