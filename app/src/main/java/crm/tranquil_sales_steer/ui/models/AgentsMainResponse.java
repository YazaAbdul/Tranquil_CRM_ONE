package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentsMainResponse implements Serializable {

    private String designation_id;
    private String designation_name;
    private String noofagents;
    ArrayList<AgentsResponse> agents = new ArrayList<>();

    public AgentsMainResponse(String designation_id, String designation_name, String noofagents, ArrayList<AgentsResponse> agents) {
        this.designation_id = designation_id;
        this.designation_name = designation_name;
        this.noofagents = noofagents;
        this.agents = agents;
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

    public ArrayList<AgentsResponse> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<AgentsResponse> agents) {
        this.agents = agents;
    }
}
