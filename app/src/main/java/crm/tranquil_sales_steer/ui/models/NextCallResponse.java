package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class NextCallResponse implements Serializable {

    private String lead_id;
    private String lead_name;
    private String mobile_number;
    private String email_id;
    private String requirement_name;
    private String source_id;
    private String activity_remarks;
    private String created_by;
    private String activity_id;
    private String activity_name;
    private String lead_pic;
    private String company_name;


    public NextCallResponse(String lead_id, String lead_name, String mobile_number, String email_id, String requirement_name, String source_id, String activity_remarks, String created_by, String activity_id, String activity_name, String lead_pic, String company_name) {
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.mobile_number = mobile_number;
        this.email_id = email_id;
        this.requirement_name = requirement_name;
        this.source_id = source_id;
        this.activity_remarks = activity_remarks;
        this.created_by = created_by;
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.lead_pic = lead_pic;
        this.company_name = company_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getRequirement_name() {
        return requirement_name;
    }

    public void setRequirement_name(String requirement_name) {
        this.requirement_name = requirement_name;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getActivity_remarks() {
        return activity_remarks;
    }

    public void setActivity_remarks(String activity_remarks) {
        this.activity_remarks = activity_remarks;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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

    public String getLead_pic() {
        return lead_pic;
    }

    public void setLead_pic(String lead_pic) {
        this.lead_pic = lead_pic;
    }
}
