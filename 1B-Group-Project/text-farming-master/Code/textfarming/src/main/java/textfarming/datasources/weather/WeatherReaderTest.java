package textfarming.datasources.weather;



import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

public class WeatherReaderTest {
    @Test
    public void weatherReceiverGetsData() throws IOException {
        Random random = new Random();
        DarkSkyResponse response = WeatherReader.getWeatherData(random.nextDouble() * 180 - 90,
                random.nextDouble() * 360 - 180);

        assertNotNull(response);
        assertNotNull(response.daily);
        assertNotNull(response.daily.data);
        assertNotEquals(response.daily.data.length, 0);
        assertNotEquals(response.daily.data.length, 1);
        assertNotEquals(response.daily.data.length, 2);

        long midnightToday = new Date().getTime();
        midnightToday = midnightToday - midnightToday % (24 * 60 * 60 * 1000);

        for (int i = 0; i < 3; i++){
            assertNotNull(response.daily.data[i]);
            assertNotNull(response.daily.data[i].summary);
            assertNotEquals("", response.daily.data[i].summary);

            // Checks that times are in the future
            assertTrue( Math.abs((response.daily.data[i].temperatureMaxTime*1000L) - midnightToday) >= 0);
        }
    }

    @Test
    public void getWeatherUpdateSucceeds() {
        Random random = new Random();
        String msg = WeatherReader.getWeatherUpdate(random.nextDouble() * 180 - 90,
                random.nextDouble() * 360 - 180, 2);


        assertNotNull(msg);
        assertNotEquals("", msg);
        assertNotEquals("Weather data unavailable", msg);

        System.out.println(msg);
    }
}
