package textfarming.datasources.market;

/**
 * The crops supported by our Crop Price reader (which uses
 * <a href="http://www.foodsecurityportal.org/api/">Food Security Portal</a>).
 */
public enum Crop {
    MAIZE, RICE;
    
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
