package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AgentsResponse implements Serializable {

    private String agen_id;
    private String agent_name;
    private String agent_mobile;
    private String agent_email;
    private String designation;

    public AgentsResponse(String agen_id, String agent_name, String agent_mobile, String agent_email, String designation) {
        this.agen_id = agen_id;
        this.agent_name = agent_name;
        this.agent_mobile = agent_mobile;
        this.agent_email = agent_email;
        this.designation = designation;
    }

    public String getAgen_id() {
        return agen_id;
    }

    public void setAgen_id(String agen_id) {
        this.agen_id = agen_id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getAgent_mobile() {
        return agent_mobile;
    }

    public void setAgent_mobile(String agent_mobile) {
        this.agent_mobile = agent_mobile;
    }

    public String getAgent_email() {
        return agent_email;
    }

    public void setAgent_email(String agent_email) {
        this.agent_email = agent_email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
