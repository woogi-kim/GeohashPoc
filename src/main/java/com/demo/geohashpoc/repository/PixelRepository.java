package com.demo.geohashpoc.repository;

import com.demo.geohashpoc.entity.Pixel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PixelRepository extends JpaRepository<Pixel, Long> {
    @Query(value = "SELECT * FROM pixel p WHERE ST_Distance_Sphere(p.coordinate, ST_PointFromText(:point, 4326)) <= :radius", nativeQuery = true)
    List<Pixel> findPixelsWithinRadius(@Param("point") String point, @Param("radius") double radius);
}
