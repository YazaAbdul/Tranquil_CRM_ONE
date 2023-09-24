package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AddQuotationResponse implements Serializable {

    private String q_id;
    private String product_name;
    private String hsn_sac;
    private String qty;
    private String price;
    private String amount;
    private String uom;
    private String disc_amt;
    private String created_by;
    private String created_date;
    private String gst_amt;
    private String gst_per;
    private String description;
    private String total_withgst;

    public AddQuotationResponse(String q_id, String product_name, String hsn_sac, String qty, String price, String amount, String uom, String disc_amt, String created_by, String created_date, String gst_amt, String gst_per, String description, String total_withgst) {
        this.q_id = q_id;
        this.product_name = product_name;
        this.hsn_sac = hsn_sac;
        this.qty = qty;
        this.price = price;
        this.amount = amount;
        this.uom = uom;
        this.disc_amt = disc_amt;
        this.created_by = created_by;
        this.created_date = created_date;
        this.gst_amt = gst_amt;
        this.gst_per = gst_per;
        this.description = description;
        this.total_withgst = total_withgst;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getHsn_sac() {
        return hsn_sac;
    }

    public void setHsn_sac(String hsn_sac) {
        this.hsn_sac = hsn_sac;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getDisc_amt() {
        return disc_amt;
    }

    public void setDisc_amt(String disc_amt) {
        this.disc_amt = disc_amt;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getGst_amt() {
        return gst_amt;
    }

    public void setGst_amt(String gst_amt) {
        this.gst_amt = gst_amt;
    }

    public String getGst_per() {
        return gst_per;
    }

    public void setGst_per(String gst_per) {
        this.gst_per = gst_per;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotal_withgst() {
        return total_withgst;
    }

    public void setTotal_withgst(String total_withgst) {
        this.total_withgst = total_withgst;
    }
}
