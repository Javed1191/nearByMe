package model_classes;

public class Security {
    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String ip_address, state, country,date,time;


    public Security(String ip_address, String state, String country, String date, String time)
    {
        this.ip_address = ip_address;
        this.state = state;
        this.country = country;
        this.date = date;
        this.time = time;
    }




}