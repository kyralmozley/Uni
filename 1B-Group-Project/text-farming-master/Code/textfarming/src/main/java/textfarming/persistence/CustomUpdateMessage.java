package textfarming.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

@Entity
public class CustomUpdateMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customUpdateMessageId;

    @Expose private double longitude;
    @Expose private double latitude;
    /**
     * Accuracy of long/lat, in metres.
     * Radius also in metres.
     */
    @Expose private double locRadius;

    @Temporal(TemporalType.TIMESTAMP)
    @Expose private Date expiryDate;
    @Expose private String message;

    protected CustomUpdateMessage() {}

    public CustomUpdateMessage(double latitude, double longitude, double locRadius, Date expiryDate, String message) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locRadius = locRadius;
        this.expiryDate = expiryDate;
        this.message = message;
    }

    public long getCustomUpdateMessageId() {
        return customUpdateMessageId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLocRadius() {
        return locRadius;
    }

    public void setLocRadius(int locRadius) {
        this.locRadius = locRadius;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
