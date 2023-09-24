package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AvailableProjectsResponse implements Serializable {

    private String requirement_id;
    private String requirement_name;
    private String available;
    private String sold;
    private String blocked;
    private String total;

    public AvailableProjectsResponse(String requirement_id, String requirement_name, String available, String sold, String blocked, String total) {
        this.requirement_id = requirement_id;
        this.requirement_name = requirement_name;
        this.available = available;
        this.sold = sold;
        this.blocked = blocked;
        this.total = total;
    }

    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getRequirement_name() {
        return requirement_name;
    }

    public void setRequirement_name(String requirement_name) {
        this.requirement_name = requirement_name;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
