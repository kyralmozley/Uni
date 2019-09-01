package group27.weatherapp.datasources.weather.mountain;

import java.util.HashMap;
import java.util.Map;

public class MountainHazards {
    private MountainHazard[] Hazard;

    /**
     * Convert the objects from DataPoint representing hazards into a Map of hazard names to probabilities
     *
     * @param includeNoChanceHazards whether to include hazards rated as "No likelihood"
     * @return a Map of Hazard names to probabilities
     */
    public Map<String, String> parseHazards(boolean includeNoChanceHazards) {
        HashMap<String, String> hazards = new HashMap<>();

        for (MountainHazard hazard : Hazard) {
            if (includeNoChanceHazards || !hazard.Likelihood.Type.equals("NO_LIKELIHOOD")) {
                hazards.put(beautify(hazard.Element.Type), beautify(hazard.Likelihood.Type));
            }
        }

        return hazards;
    }

    /**
     * Tidy up all caps and underscored strings into a lower case string with a leading capital letter, and replacing
     * underscores for spaces
     *
     * @param s a string to tidy up
     * @return the new string
     */
    private static String beautify(String s) {
        s = s.trim().replaceAll("_", " ").toLowerCase();

        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
