package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class CallHistoryResponse implements Serializable {

    private String lead_id;
    private String customer_name;
    private String mobile_number;
    private String cal_start_time;
    private String cal_end_time;
    private String time_in_sec;
    private String time_gap;
    private String color;

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCal_start_time() {
        return cal_start_time;
    }

    public void setCal_start_time(String cal_start_time) {
        this.cal_start_time = cal_start_time;
    }

    public String getCal_end_time() {
        return cal_end_time;
    }

    public void setCal_end_time(String cal_end_time) {
        this.cal_end_time = cal_end_time;
    }

    public String getTime_in_sec() {
        return time_in_sec;
    }

    public void setTime_in_sec(String time_in_sec) {
        this.time_in_sec = time_in_sec;
    }

    public String getTime_gap() {
        return time_gap;
    }

    public void setTime_gap(String time_gap) {
        this.time_gap = time_gap;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
