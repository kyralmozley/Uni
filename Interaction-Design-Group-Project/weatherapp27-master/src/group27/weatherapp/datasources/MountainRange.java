package group27.weatherapp.datasources;

import group27.weatherapp.datasources.location.InMountainRange;

/**
 * An enum representing the mountain regions available in the Met Office data.
 * Each one is associated with a representative point and a size, treated as a radius for checking bounds.
 */
public enum MountainRange {
    Brecon_Beacons(51.872285, -3.472651, 32500),
    Lake_District(54.484612, -3.076282, 31000),
    North_Grampian(57.285920, -3.830108, 46200),
    Northwest_Highlands(57.914321, -5.664864, 100000),
    South_Grampian_and_Southeast_Highlands(56.592933, -3.225006, 58900),
    Southwest_Highlands(56.559344, -5.403525, 91775),
    Peak_District(53.314993, -1.808827, 27755),
    Snowdonia(52.919729, -3.915223, 35000),
    Yorkshire_Dales(54.484612, -3.076282, 35000),
    Mourne_Mountains(54.084416, -6.017764, 16500);

    MountainRange(double lat, double lon, double s) {
        this.lat = lat;
        this.lon = lon;
        this.size = s;
    }

    public final double lat;
    public final double lon;
    public final double size;

    /**
     * Checks whether the coordinate (lat, lon) is in this mountain region, approximating it as a circle.
     */
    public boolean in(double lat, double lon) {
        return InMountainRange.distanceBetweenCoords(lat, lon, this.lat, this.lon) <= this.size;
    }
}
