package textfarming.sms.registration.location;

public class LatLongAcc {
    
    public final double lat;
    public final double lng;
    public final int acc;
    
    public LatLongAcc(double lat, double lng, int acc) {
        this.lat = lat;
        this.lng = lng;
        this.acc = acc;
    }
    
    @Override
    public String toString() {
        return "lat: " + lat + ", lng: " + lng + ", acc: " + acc;
    }
}
