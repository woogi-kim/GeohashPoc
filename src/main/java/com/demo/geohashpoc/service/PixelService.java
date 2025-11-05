package com.demo.geohashpoc.service;

import com.demo.geohashpoc.entity.Pixel;
import com.demo.geohashpoc.repository.PixelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PixelService {

    private final PixelRepository pixelRepository;

    public List<Pixel> findPixelsWithinRadius(double longitude, double latitude, double radius) {
        String point = String.format("POINT(%f %f)", longitude, latitude);
        return pixelRepository.findPixelsWithinRadius(point, radius);
    }
}
