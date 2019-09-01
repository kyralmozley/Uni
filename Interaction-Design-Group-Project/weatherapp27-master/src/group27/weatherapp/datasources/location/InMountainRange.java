package group27.weatherapp.datasources.location;

import group27.weatherapp.datasources.MountainRange;

public class InMountainRange {

    /**
     * Determine which mountain range, if any a point is in
     *
     * @param lat The point's latitude
     * @param lon The point's longitude
     * @return The mountain range, or null if not in any
     */
    public static MountainRange inMountainRange(double lat, double lon) {
        for (MountainRange range : MountainRange.values()) {
            if (range.in(lat, lon)) return range;
        }

        return null;
    }

    /**
     * Helper function to find the distance between two lat-long points on Earth
     *
     * @param lat1 latitude of point 1
     * @param lon1 longitude of point 1
     * @param lat2 latitude of point 2
     * @param lon2 longitude of point 2
     * @return distance in metres
     */
    public static double distanceBetweenCoords(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371e3; // Meters
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;

        double a = Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(lonDiff / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}