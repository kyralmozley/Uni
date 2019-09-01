package textfarming.datasources.market;

/**
 * A Java class in the same structure as an individual currency conversion element from the JSON data received from the
 * Currency Converter API when requesting a currency conversion.
 * Most of the information we get is ignored and not used
 *
 * @author Benji
 * @see <a href="http://currencyconverterapi.com/">Currency Converter API</a>
 */
public class CurrencyConversionItem {
    String id;
    double val;
    String to;
    String fr;
}
