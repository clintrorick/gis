-- assumption: representing a city as a single point, not a polygon, for simplicity's sake - a production impl would use a polygon
create table gis.cities_coords (
    city_name text,
    city_point geometry
);

insert into gis.cities_coords (city_name, city_point) values( 'Tokyo', 'POINT(139.83947 35.65283)');
insert into gis.cities_coords (city_name, city_point) values( 'Sydney', 'POINT(151.20732 -33.86785)');
insert into gis.cities_coords (city_name, city_point) values( 'Riyadh', 'POINT(46.72185 24.68773)');
insert into gis.cities_coords (city_name, city_point) values( 'Zurich', 'POINT(8.55 47.36667)');
insert into gis.cities_coords (city_name, city_point) values( 'Reykvjavik', 'POINT(-21.827774 64.128288)');
insert into gis.cities_coords (city_name, city_point) values( 'Mexico City', 'POINT( -99.133209 19.432608)');
insert into gis.cities_coords (city_name, city_point) values( 'Lima', 'POINT(-77.042793 -12.046374)');
