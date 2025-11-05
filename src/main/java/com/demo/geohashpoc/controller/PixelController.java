package com.demo.geohashpoc.controller;

import com.demo.geohashpoc.entity.Pixel;
import com.demo.geohashpoc.service.PixelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pixels")
public class PixelController {

    private final PixelService pixelService;

    @GetMapping("/search")
    public List<Pixel> getPixelsWithinRadius(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam double radius) {
        return pixelService.findPixelsWithinRadius(longitude, latitude, radius);
    }
}
