package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 28-Aug-19.
 */
public class CreateLeadResponse {
    /*lead_id: 4,
status: 1*/

    private String lead_id;
    private String status;

    public CreateLeadResponse(String lead_id, String status) {
        this.lead_id = lead_id;
        this.status = status;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
