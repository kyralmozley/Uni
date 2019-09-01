package textfarming.datasources.market;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

import static org.junit.Assert.*;

public class CurrencyConverterTest {
    private CurrencyConverter instance;

    @Before
    public void initialise() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        instance = CurrencyConverter.getInstance();
    }

    @Test
    public void canGetCountryUK() {
        CurrencyConverterCountry ccc = instance.getCountry(Locale.UK);

        assertNotNull(ccc);
        assertNotNull(ccc.alpha3);
        assertEquals("GBR", ccc.alpha3);

        assertNotNull(ccc.currencyId);
        assertEquals("GBP", ccc.currencyId);

        assertNotNull(ccc.currencyName);
        assertEquals("British pound", ccc.currencyName);

    }

    @Test
    public void canGetCountryUganda()  {
        CurrencyConverterCountry ccc = instance.getCountry(new Locale("", "UG"));

        assertNotNull(ccc);
        assertNotNull(ccc.alpha3);
        assertEquals("UGA", ccc.alpha3);

        assertNotNull(ccc.currencyId);
        assertEquals("UGX", ccc.currencyId);

        assertNotNull(ccc.currencyName);
        assertEquals("Ugandan shilling", ccc.currencyName);
    }

    @Test
    public void canGetCountryUSA() {
        CurrencyConverterCountry ccc = instance.getCountry(new Locale("", "US"));

        assertNotNull(ccc);
        assertNotNull(ccc.alpha3);
        assertEquals("USA", ccc.alpha3);

        assertNotNull(ccc.currencyId);
        assertEquals("USD", ccc.currencyId);

        assertNotNull(ccc.currencyName);
        assertEquals("United States dollar", ccc.currencyName);
    }

    @Test
    public void canGetRatioUK() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        CurrencyConverterCountry uk = instance.getCountry(Locale.UK);
        CurrencyConverterCountry us = instance.getCountry(Locale.US);
        assertNotNull(uk);
        assertNotNull(us);

        double ratio = instance.getConversionRatio(uk, us);

        System.out.println(ratio + " GBP per USD");

        assertTrue(0.7 <= ratio && ratio <= 2);
    }

    @Test
    public void canGetRatioUganda() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        CurrencyConverterCountry ug = instance.getCountry(new Locale("", "UG"));
        CurrencyConverterCountry us = instance.getCountry(Locale.US);
        assertNotNull(ug);
        assertNotNull(us);

        double ratio = instance.getConversionRatio(ug, us);

        System.out.println(ratio + " UGX per USD");

        assertTrue(0.00005 <= ratio && ratio <= 0.001);
    }

    @Test
    public void canConvertCurrencyUK() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        Random random = new Random();
        double amount = random.nextDouble() * 100;
        String s = CurrencyConverter.convertCurrency(amount, Locale.US, Locale.UK);

        CurrencyConverterCountry uk = instance.getCountry(Locale.UK);
        CurrencyConverterCountry us = instance.getCountry(Locale.US);
        assertNotNull(uk);
        assertNotNull(us);

        double ratio = instance.getConversionRatio(us, uk);
        DecimalFormat df = new DecimalFormat("0.00");

        assertNotNull(s);
        assertEquals( df.format(ratio*amount) + " British pound", s);
    }

    @Test
    public void canConvertCurrencyUganda() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        Random random = new Random();
        double amount = random.nextDouble() * 100;
        String s = CurrencyConverter.convertCurrency(amount, Locale.US, new Locale("", "UG"));

        CurrencyConverterCountry us = instance.getCountry(Locale.US);
        CurrencyConverterCountry ug = instance.getCountry(new Locale("", "UG"));
        assertNotNull(us);
        assertNotNull(ug);

        double ratio = instance.getConversionRatio(us, ug);

        DecimalFormat df = new DecimalFormat("0.00");

        assertNotNull(s);
        assertEquals( df.format(ratio*amount) + " Ugandan shilling", s);
    }
}
