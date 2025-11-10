package com.demo.geohashpoc.entity;

import com.demo.geohashpoc.config.PointSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pixel", indexes = {
        @Index(name = "idx_geohash", columnList = "geohash"),
        @Index(name = "idx_x_y", columnList = "x, y")
})
public class Pixel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pixel_id")
    private Long id;

    @Column(nullable = false)
    @JsonSerialize(using = PointSerializer.class)
    private Point coordinate;

    private Long x;

    private Long y;

    private String geohash;

    private LocalDateTime createdAt;

    public Pixel(Point coordinate, Long x, Long y, String geohash) {
        this.coordinate = coordinate;
        this.x = x;
        this.y = y;
        this.geohash = geohash;
        this.createdAt = LocalDateTime.now();
    }
}
