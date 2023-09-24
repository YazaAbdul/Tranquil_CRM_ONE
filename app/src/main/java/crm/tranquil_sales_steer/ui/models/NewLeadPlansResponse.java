package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class NewLeadPlansResponse implements Serializable {

    private String plan_id;
    private String plan_name;
    private String count;

    public NewLeadPlansResponse(String plan_id, String plan_name, String count) {
        this.plan_id = plan_id;
        this.plan_name = plan_name;
        this.count = count;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
