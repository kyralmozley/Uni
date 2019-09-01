package textfarming.datasources.market;

/**
 * A Java class in the same structure as an individual Country element from the JSON data received from the Currency
 * Converter API when requesting a country list. Most of the information we get is ignored and not used
 *
 * @author Benji
 * @see <a href="http://currencyconverterapi.com/">Currency Converter API</a>
 */
public class CurrencyConverterCountry {

    /**
     * The ISO 3166-1 alpha-3 code for the country, which are three letter codes
     */
    String alpha3;

    /**
     * The ISO 4217 code for the currency, which are three letter codes
     */
    String currencyId;

    /**
     * The English name of the currency
     */
    String currencyName;
    
    // TODO: Our SMSSender does correctly handle UTF-8. Any changes to make?
    // TODO: Our SMS sending functionality does cope with UTF-8, should we make use of that?

    /**
     * The currency symbol. Often some UTF-8 symbol that SMS cannot parse
     */
    String currencySymbol;
    /**
     * The ISO 3166-1 alpha-2 code for the country, which are two letter codes
     */
    String id;

    /**
     * The English name of the country
     */
    String name;
}
