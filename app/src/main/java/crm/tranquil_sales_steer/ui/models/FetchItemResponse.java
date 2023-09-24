package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class FetchItemResponse implements Serializable {

    private String description;
    private String hsn;
    private String unit_price;
    private String gst;
    private String uom;
    private String status;

    public FetchItemResponse(String description, String hsn, String unit_price, String gst, String uom, String status) {
        this.description = description;
        this.hsn = hsn;
        this.unit_price = unit_price;
        this.gst = gst;
        this.uom = uom;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
