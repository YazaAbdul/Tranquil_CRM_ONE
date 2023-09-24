package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AddQuotationTotalResponse implements Serializable {

    private String amount;
    private String discount;
    private String after_discount;
    private String gst;
    private String total_withgst;

    public AddQuotationTotalResponse(String amount, String discount, String after_discount, String gst, String total_withgst) {
        this.amount = amount;
        this.discount = discount;
        this.after_discount = after_discount;
        this.gst = gst;
        this.total_withgst = total_withgst;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAfter_discount() {
        return after_discount;
    }

    public void setAfter_discount(String after_discount) {
        this.after_discount = after_discount;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTotal_withgst() {
        return total_withgst;
    }

    public void setTotal_withgst(String total_withgst) {
        this.total_withgst = total_withgst;
    }
}
