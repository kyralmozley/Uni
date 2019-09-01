package group27.weatherapp.datasources.weather.mountain;

public class MountainPeriod {
    String End;
    String Start;
    MountainPrecipitation Precipitation;
    MountainHeight[] Height;
    String FreezingLevel;

    public String getStart() {
        return Start;
    }

    public String getPrecipitationProb() {
        return Precipitation.Probability;
    }

    public MountainHeight[] getHeight() {
        return Height;
    }

    public String getFreezingLevel() {
        return FreezingLevel;
    }
}
