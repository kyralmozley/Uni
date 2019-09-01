package group27.weatherapp.datasources.weather.mountain;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MountainDay {
    // All days
    String Validity;

    // Only today
    String Headline;

    // Only today
    String Confidence;

    // Only today
    String View;

    // Only today
    String CloudFreeHillTop;

    // Only tomorrow, textual
    String wind;

    // Only tomorrow
    String HillCloud;

    // Only today and tomorrow
    String Weather;

    // Only today and tomorrow, textual. Probably better to use the distance from DarkSky which are the "Basic" classes
    String Visibility;

    // Only today
    MountainHazards Hazards;

    // Only today
    MountainPeriods Periods;

    // Only today
    MountainGroundConditions GroundConditions;

    // Only days after tomorrow
    String Summary;

    public String getWeather() {
        return Weather;
    }

    public String getValidity() {
        return Validity;
    }

    public MountainHazards getHazards() {
        return Hazards;
    }

    public MountainPeriod[] getPeriods() {
        return Periods.Period;
    }

    public String getSummary() {
        return Summary;
    }


    /**
     * For a forecast, find the forecast for a specific 3 hour-ly time period
     *
     * @param time The time the forecast is wanted for
     * @return A representation of the mountain's forecast for that period
     */
    public MountainPeriod getSinglePeriodData(Date time) {
        Calendar givenTime = Calendar.getInstance();
        givenTime.setTime(time);

        for (MountainPeriod period : Periods.Period) {
            StringBuilder current = new StringBuilder(getValidity());
            current.delete(current.length() - 6, current.length());
            current.append(period.getStart());

            try {
                Calendar hourlyPointTime = Calendar.getInstance();
                hourlyPointTime.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(current.toString()));
                System.out.println("Trying: " + hourlyPointTime.getTime() + " to fit " + time);

                int hourDiff = givenTime.get(Calendar.HOUR_OF_DAY) - hourlyPointTime.get(Calendar.HOUR_OF_DAY);
                if (hourlyPointTime.get(Calendar.DAY_OF_YEAR) == givenTime.get(Calendar.DAY_OF_YEAR)
                        && 0 <= hourDiff && hourDiff < 3) {
                    return period;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
