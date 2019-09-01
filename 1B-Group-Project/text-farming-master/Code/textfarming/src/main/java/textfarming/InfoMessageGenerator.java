package textfarming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import textfarming.datasources.coordstocountry.CoordsToCountry;
import textfarming.datasources.market.Crop;
import textfarming.datasources.market.CropPriceReader;
import textfarming.datasources.weather.WeatherReader;
import textfarming.persistence.CustomUpdateMessage;
import textfarming.persistence.CustomUpdateMessageService;
import textfarming.persistence.User;
import textfarming.persistence.UserService;

/**
 * A class containing a function that collates weather and crop price information for a given location.
 *
 * @see CropPriceReader
 * @see WeatherReader
 * @author Benji
 */
public class InfoMessageGenerator {

    /**
     * Generates an info message for a given location, and crop preferences
     *
     * @param lat Latitude of the point in degrees
     * @param lon Longitude of the point in degrees
     * @param whichCrops A Map of {@link Crop} types to {@code boolean} values to indicate whether that crop price
     *                   is requested (where {@code true} implies that information is requested, and a {@link Crop} not
     *                   being present implies that it is not required).
     * @return The information message
     */
    private static String generateMessage(double lat, double lon, Map<Crop, Boolean> whichCrops) {
        StringBuilder message = new StringBuilder();

        String weatherData = WeatherReader.getWeatherUpdate(lat, lon, 2);
        message.append(weatherData);

        Locale locale = CoordsToCountry.coordsToLocale(lat, lon);

        if (locale == null) {
            message.append("Crop data unavailable").append("\n");
        }
        else for (Crop crop : Crop.values()) {
            if (whichCrops.getOrDefault(crop, false)){
                message.append(CropPriceReader.getCropPrice(locale, crop)).append("\n\n");
            }
        }
        message.deleteCharAt(message.length() - 1);

        return message.toString();
    }

    /**
     * Finds the distance between latitude-longitude coordinates.
     *
     * @param lat1 Latitude of the first point in degrees
     * @param lon1 Longitude of the first point in degrees
     * @param lat2 Latitude of the second point in degrees
     * @param lon2 Longitude of the second point in degrees
     * @return The distance between the two points in metres
     */
    public static double distanceBetweenCoords(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS = 6371e3; // Meters
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;

        double a = Math.pow(Math.sin(latDiff/2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(lonDiff/2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return EARTH_RADIUS * c;
    }

    /**
     * Generates a info message for a {@link User} in the database, including any relevant custom messages received from
     * the web interface
     *
     * @param tel The telephone number of the user, which is the primary key of the database
     * @param userService The user database service
     * @param customUpdateMessageService The Custom Update Message database service
     * @return The info message
     */
    public static String generateUserMessage(String tel, UserService userService,
            CustomUpdateMessageService customUpdateMessageService) {
        StringBuilder message = new StringBuilder();
        User user = userService.getUser(tel);

        Map<Crop, Boolean> whichCrops = new HashMap<>();
        for (Crop crop : Crop.values())
            if (user.grows(crop)) whichCrops.put(crop, true);

        message.append(generateMessage(user.getLat(), user.getLng(), whichCrops));

        for (CustomUpdateMessage customMessage : customUpdateMessageService.getAllMessages()) {
            // If the user is in the message's radius
            if (distanceBetweenCoords(user.getLat(), user.getLng(),
                    customMessage.getLatitude(), customMessage.getLongitude()) <= customMessage.getLocRadius()) {
                message.append("\n\n").append(customMessage.getMessage());
            }
        }

        return message.toString();
    }

    public static List<String> splitMessage(String message) {
        List<String> segList = new ArrayList<>();
        for (String seg_unfiltered : message.split("\n"))
            if (seg_unfiltered.length() > 0)
                segList.add(seg_unfiltered);
        return segList;
    }

}
