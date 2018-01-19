package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class LocationPoints implements Serializable {

    private String feedUserName,tokLatitude,tokLongitude,distance;


    public LocationPoints(String feedUserName,String tokLatitude, String tokLongitude, String distance )
    {
        this.feedUserName = feedUserName;
        this.tokLatitude=tokLatitude;
        this.tokLongitude=tokLongitude;
        this.distance=distance;
    }

    public String getUserName() {
        return feedUserName;
    }

    public void setUserName(String feedUserName) {
        this.feedUserName = feedUserName;
    }

    public String getTokLatitude() {
        return tokLatitude;
    }

    public void setTokLatitude(String tokLatitude) {
        this.tokLatitude = tokLatitude;
    }

    public String getTokLongitude() {
        return tokLongitude;
    }

    public void setTokLongitude(String tokLongitude) {
        this.tokLongitude = tokLongitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


}