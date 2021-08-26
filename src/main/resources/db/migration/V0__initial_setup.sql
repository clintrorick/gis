create schema if not exists gis;

drop table if exists gis.points;

create table gis.points (
    id bigserial,
    point geometry
);

--The polygon supplied to ST_GeomFromText is a rectangle that roughly covers most of the populated areas of the earth.  This polygon is not intended to be fully inclusive.
-- You can view the polygon via this link:
-- http://geojson.io/#id=gist:clintrorick/753d71cbafdea31a23c2a97529bd7185&map=2/25.5/0.4

--ST_GeneratePoints accepts that polygon and generates 10,000 random points within that polygon.
--The third parameter to ST_GeneratePoints is the seed, 1234, which ensures that the same 10,000 points are generated each time.
--This enables us to easily write deterministic automated tests.  To change the random set of data, simply change the seed from '1234' to any other integer.

--ST_DumpPoints is a glue function that allows us to break the output of ST_GeneratePoints into individual records, to be inserted into gis.points.

insert into gis.points (point)
select (ST_DumpPoints(
            ST_GeneratePoints(
                popd_earth_polygon,
                10000,
                1234)
            )
        ).geom
from (select * from ST_GeomFromText('POLYGON((-163 -54, 163 -54, 163 75, -163 75, -163 -54))') as popd_earth_polygon)
   as x;


