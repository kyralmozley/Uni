package textfarming.datasources.market;

import java.util.Map;

/**
 * A Java class in the same structure as the JSON data received from the Currency Converter API when requesting a list
 * of supported countries.
 *
 * @author Benji
 * @see CurrencyConverterCountry
 * @see <a href="http://currencyconverterapi.com/">Currency Converter API</a>
 */
public class CurrencyConverterCountryResponse {

    /**
     * The list that we receive is indexed by two letter country codes. We will be ignoring these keys, but we need to
     * make this structure for GSon to know how to parse it.
     */
    Map<String, CurrencyConverterCountry> results;
}
