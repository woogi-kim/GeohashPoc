package com.demo.geohashpoc.service;

import com.demo.geohashpoc.entity.Pixel;
import com.demo.geohashpoc.repository.PixelRepository;
import com.demo.geohashpoc.util.GeoHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PixelService {

    private final PixelRepository pixelRepository;

    public List<Pixel> findPixelsWithinRadius(double longitude, double latitude, double radius) {
        String point = String.format("POINT(%f %f)", latitude, longitude);
        List<Pixel> list = pixelRepository.findPixelsWithinRadius(point, radius);
		return list;
    }

    public List<Pixel> findPixelsByGeohash(double latitude, double longitude, int radius) {
        String geohashPrefix = GeoHashUtil.getBoundingGeoHash(latitude, longitude, radius);
        return pixelRepository.findPixelsByGeohashPrefix(geohashPrefix);
    }
}
