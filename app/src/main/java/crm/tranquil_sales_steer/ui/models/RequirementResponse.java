package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 06-Jul-19.
 */
public class RequirementResponse {

    /*requirement_id: "1",
requirement_name: "Sample 1"*/

    private String requirement_id;
    private String requirement_name;

    public RequirementResponse(String requirement_id, String requirement_name) {
        this.requirement_id = requirement_id;
        this.requirement_name = requirement_name;
    }

    public RequirementResponse() {

    }

    public String getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(String requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getRequirement_name() {
        return requirement_name;
    }

    public void setRequirement_name(String requirement_name) {
        this.requirement_name = requirement_name;
    }
}
