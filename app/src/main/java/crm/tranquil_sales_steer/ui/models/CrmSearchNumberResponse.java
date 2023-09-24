package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 09-Aug-19.
 */
public class CrmSearchNumberResponse {

    /*lead_id: "40",
lead_name: "venkei test",
lead_email: "venkei25@gmail.com",
lead_mobile: "9618675200"*/

    private String lead_id;
    private String lead_name;
    private String lead_email;
    private String lead_mobile;

    public CrmSearchNumberResponse(String lead_id, String lead_name, String lead_email, String lead_mobile) {
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.lead_email = lead_email;
        this.lead_mobile = lead_mobile;
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
}
