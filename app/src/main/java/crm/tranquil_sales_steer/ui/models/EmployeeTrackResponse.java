package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class EmployeeTrackResponse implements Serializable {

    private String area;
    private String location;
    private String longitude;
    private String latitude;
    private String date;
    private String created_date;

    public EmployeeTrackResponse(String area, String location, String longitude, String latitude, String date, String created_date) {
        this.area = area;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
        this.created_date = created_date;
    }

    public EmployeeTrackResponse() {

    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
