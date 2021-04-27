CREATE FUNCTION points_distance(
    latitude1 double precision,
    longitude1 double precision,
    latitude2 double precision,
    longitude2 double precision
) RETURNS double precision
AS '
    select earth_distance(
        ll_to_earth(latitude1, longitude1),
        ll_to_earth(latitude2, longitude2)
    );'
LANGUAGE SQL;

CREATE FUNCTION first_fix_point_distance(
    latitude double precision,
    longitude double precision
) RETURNS double precision
AS '
    select points_distance(latitude, longitude, 0, 0)
;'
LANGUAGE SQL;

CREATE FUNCTION second_fix_point_distance(
    latitude double precision,
    longitude double precision
) RETURNS double precision
AS '
    select points_distance(latitude, longitude, 0, 90)
;'
LANGUAGE SQL;

CREATE INDEX first_fix_point_distance_index
    ON nodes (first_fix_point_distance(latitude, longitude));
CREATE INDEX second_fix_point_distance_index
    ON nodes (second_fix_point_distance(latitude, longitude));

CREATE FUNCTION find_nearest_nodes(
    lat double precision,
    lon double precision,
    radius double precision
) RETURNS SETOF nodes
AS '
    select * from (
        select * from nodes
        where first_fix_point_distance(latitude, longitude)
            between first_fix_point_distance(lat, lon) - radius and first_fix_point_distance(lat, lon) + radius
        and second_fix_point_distance(latitude, longitude)
            between second_fix_point_distance(lat, lon) - radius and second_fix_point_distance(lat, lon) + radius
    ) as nearest_nodes
    where points_distance(nearest_nodes.latitude, nearest_nodes.longitude, lat, lon) < radius
;'
LANGUAGE SQL;
