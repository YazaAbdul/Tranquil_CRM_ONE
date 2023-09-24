package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class SourceTypeLeadsResponse implements Serializable {

    private String lead_id;
    private String lead_name;
    private String lead_email;
    private String lead_mobile;
    private String activity_id;
    private String activity_name;
    private String activity_date;
    private String activity_time;

    public SourceTypeLeadsResponse(String lead_id, String lead_name, String lead_email, String lead_mobile, String activity_id, String activity_name, String activity_date, String activity_time) {
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.lead_email = lead_email;
        this.lead_mobile = lead_mobile;
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.activity_date = activity_date;
        this.activity_time = activity_time;
    }

    public SourceTypeLeadsResponse() {

    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getLead_name() {
        return lead_name;
    }

    public void setLead_name(String lead_name) {
        this.lead_name = lead_name;
    }

    public String getLead_email() {
        return lead_email;
    }

    public void setLead_email(String lead_email) {
        this.lead_email = lead_email;
    }

    public String getLead_mobile() {
        return lead_mobile;
    }

    public void setLead_mobile(String lead_mobile) {
        this.lead_mobile = lead_mobile;
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
