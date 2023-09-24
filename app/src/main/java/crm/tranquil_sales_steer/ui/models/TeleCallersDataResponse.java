package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class TeleCallersDataResponse implements Serializable {

    /*"caller_id": "6",
    "lead_name": "test1",
    "lead_mobile": "9988554455",
    "lead_email": "test@rma.com",
    "assigned_to": "2"*/

    private String caller_id;
    private String lead_name;
    private String lead_mobile;
    private String lead_email;
    private String assigned_to;

    public TeleCallersDataResponse(String caller_id, String lead_name, String lead_mobile, String lead_email, String assigned_to) {
        this.caller_id = caller_id;
        this.lead_name = lead_name;
        this.lead_mobile = lead_mobile;
        this.lead_email = lead_email;
        this.assigned_to = assigned_to;
    }

    public TeleCallersDataResponse() {

    }

    public String getCaller_id() {
        return caller_id;
    }

    public void setCaller_id(String caller_id) {
        this.caller_id = caller_id;
    }

    public String getLead_name() {
        return lead_name;
    }

    public void setLead_name(String lead_name) {
        this.lead_name = lead_name;
    }

    public String getLead_mobile() {
        return lead_mobile;
    }

    public void setLead_mobile(String lead_mobile) {
        this.lead_mobile = lead_mobile;
    }

    public String getLead_email() {
        return lead_email;
    }

    public void setLead_email(String lead_email) {
        this.lead_email = lead_email;
    }

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }
}
