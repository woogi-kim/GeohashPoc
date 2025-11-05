DELIMITER //

CREATE PROCEDURE generate_pixels()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE j INT DEFAULT 0;
    DECLARE lat_per_pixel DOUBLE DEFAULT 0.000724;
    DECLARE lon_per_pixel DOUBLE DEFAULT 0.000909;
    DECLARE upper_left_lat DOUBLE DEFAULT 38.240675;
    DECLARE upper_left_lon DOUBLE DEFAULT 125.905952;
    DECLARE current_lat DOUBLE;
    DECLARE current_lon DOUBLE;
    DECLARE current_point_wkt VARCHAR(255);

    WHILE i < 7000 DO
        SET j = 0;
        WHILE j < 4156 DO
            SET current_lat = upper_left_lat - (i * lat_per_pixel);
            SET current_lon = upper_left_lon + (j * lon_per_pixel);
            SET current_point_wkt = CONCAT('POINT(', current_lon, ' ', current_lat, ')');

            INSERT INTO poc.pixel (x, y, coordinate, geohash, created_at)
            VALUES (j, i, ST_PointFromText(current_point_wkt, 4326), ST_GeoHash(current_lon, current_lat, 12), NOW());

            SET j = j + 1;
        END WHILE;
        SET i = i + 1;
    END WHILE;

END //

DELIMITER ;
