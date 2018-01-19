package model_classes;

public class Transaction {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean is_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    private String title,date,price;
    boolean is_valid;


    public Transaction(String title, String date, String price, boolean is_valid)
    {
        this.title = title;
        this.date = date;
        this.price = price;
        this.is_valid = is_valid;
    }




}