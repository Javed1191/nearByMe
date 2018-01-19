package model_classes;

public class Exposure {


    private  String sector_id ="";

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getSector_symbol() {
        return sector_symbol;
    }

    public void setSector_symbol(String sector_symbol) {
        this.sector_symbol = sector_symbol;
    }

    public String getSector_description() {
        return sector_description;
    }

    public void setSector_description(String sector_description) {
        this.sector_description = sector_description;
    }

    public String getSector_name() {
        return sector_name;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }

    public String getSector_percentage() {
        return sector_percentage;
    }

    public void setSector_percentage(String sector_percentage) {
        this.sector_percentage = sector_percentage;
    }

    private String sector_symbol="";
    private String sector_description="";
    private String sector_name="";
    private String sector_percentage="";

    public Exposure(String sector_id, String sector_symbol, String sector_description, String sector_name,
                    String sector_percentage)
    {
        this.sector_id = sector_id;
        this.sector_symbol = sector_symbol;
        this.sector_description = sector_description;
        this.sector_name = sector_name;
        this.sector_percentage = sector_percentage;
    }




}