package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class DesignationResponse implements Serializable {

    public String designation_id;
    public String designation_name;
    public String noofagents;

    public DesignationResponse(String designation_id, String designation_name, String noofagents) {
        this.designation_id = designation_id;
        this.designation_name = designation_name;
        this.noofagents = noofagents;
    }

    public String getDesignation_id() {
        return designation_id;
    }

    public void setDesignation_id(String designation_id) {
        this.designation_id = designation_id;
    }

    public String getDesignation_name() {
        return designation_name;
    }

    public void setDesignation_name(String designation_name) {
        this.designation_name = designation_name;
    }

    public String getNoofagents() {
        return noofagents;
    }

    public void setNoofagents(String noofagents) {
        this.noofagents = noofagents;
    }
}
