package classes;

import java.io.Serializable;

public class Search implements Serializable {
    public Search(String address,
                  String city,String lat, String log, String title)
    {
        this.address = address;
        this.city = city;
        this.lat = lat;
        this.log = log;
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    private String address;
    private String title;
    private String area;
    private String city;
    private String lat;
    private String log;

}