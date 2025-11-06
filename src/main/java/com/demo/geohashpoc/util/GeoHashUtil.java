package com.demo.geohashpoc.util;

import ch.hsr.geohash.GeoHash;

public class GeoHashUtil {
	private static final double EARTH_RADIUS_M = 6371000.0;

	public static String getBoundingGeoHash(double lat, double lon, int radiusMeters) {
		double deltaLat = Math.toDegrees(radiusMeters / EARTH_RADIUS_M);
		double deltaLon = Math.toDegrees(radiusMeters / (EARTH_RADIUS_M * Math.cos(Math.toRadians(lat))));

		double latNorth = lat + deltaLat;
		double latSouth = lat - deltaLat;
		double lonEast = lon + deltaLon;
		double lonWest = lon - deltaLon;

		String nw = GeoHash.withCharacterPrecision(latNorth, lonWest, 12).toBase32();
		String ne = GeoHash.withCharacterPrecision(latNorth, lonEast, 12).toBase32();
		String sw = GeoHash.withCharacterPrecision(latSouth, lonWest, 12).toBase32();
		String se = GeoHash.withCharacterPrecision(latSouth, lonEast, 12).toBase32();

		return commonPrefix(nw, ne, sw, se);
	}

	private static String commonPrefix(String... hashes) {
		if (hashes == null || hashes.length == 0) return "";
		String prefix = hashes[0];
		for (int i = 1; i < hashes.length; i++) {
			prefix = getCommonPrefix(prefix, hashes[i]);
		}
		return prefix;
	}

	private static String getCommonPrefix(String a, String b) {
		int minLen = Math.min(a.length(), b.length());
		int i = 0;
		while (i < minLen && a.charAt(i) == b.charAt(i)) i++;
		return a.substring(0, i);
	}

}
