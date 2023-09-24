package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AutoFollowupResponse implements Serializable {

    private String id;
    private String lead_id;
    private String customer_name;
    private String mobile_number;
    private String activity_id;
    private String activity_name;
    private String activity_date;
    private String activity_time;

    public AutoFollowupResponse(String id, String lead_id, String customer_name, String mobile_number, String activity_id, String activity_name, String activity_date, String activity_time) {
        this.id = id;
        this.lead_id = lead_id;
        this.customer_name = customer_name;
        this.mobile_number = mobile_number;
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.activity_date = activity_date;
        this.activity_time = activity_time;
    }

    public AutoFollowupResponse() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
    }

    public String getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(String activity_time) {
        this.activity_time = activity_time;
    }
}
