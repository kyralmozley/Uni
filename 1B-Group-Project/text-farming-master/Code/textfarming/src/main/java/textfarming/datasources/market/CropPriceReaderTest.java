package textfarming.datasources.market;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class CropPriceReaderTest {
    @Test
    public void cropPriceReaderSucceedsUganda() {
        Locale locale = new Locale("", "Uganda");

        String rice = CropPriceReader.getCropPrice(locale, Crop.RICE);
        String maize = CropPriceReader.getCropPrice(locale, Crop.MAIZE);

        System.out.println(rice);
        System.out.println(maize);

        assertNotNull(rice);
        assertNotNull(maize);

        assertNotEquals(rice, "Market data unavailable");
        assertNotEquals(maize, "Market data unavailable");

        assertNotEquals(rice, "No Rice data found for " + locale.getDisplayCountry());
        assertNotEquals(maize, "No Maize data found for " + locale.getDisplayCountry());
    }

    @Test
    public void cropPriceReaderSucceedsUK() {
        Locale locale = new Locale("", "United Kingdom");

        String rice = CropPriceReader.getCropPrice(locale, Crop.RICE);
        String maize = CropPriceReader.getCropPrice(locale, Crop.MAIZE);

        System.out.println(rice);
        System.out.println(maize);

        assertNotNull(rice);
        assertNotNull(maize);

        assertNotEquals(rice, "Market data unavailable");
        assertNotEquals(maize, "Market data unavailable");

        Assert.assertEquals(rice, "No Rice data found for " + locale.getDisplayCountry());
        Assert.assertEquals(maize, "No Maize data found for " + locale.getDisplayCountry());
    }
}
