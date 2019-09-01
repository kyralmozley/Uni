package textfarming.datasources.market;

import java.util.Map;

/**
 * A Java class in the same structure as the JSON data received from the Currency Converter API to convert currencies.
 *
 * @author Benji
 * @see <a href="http://currencyconverterapi.com/">Currency Converter API</a>
 * @see CurrencyConversionItem
 */
public class CurrencyConversionResponse {

    /**
     * The list that we receive is indexed by the name of the currency conversion. We will be ignoring these keys,
     * but we need to make this structure for GSon to know how to parse it.
     */
    Map<String, CurrencyConversionItem> results;
}
