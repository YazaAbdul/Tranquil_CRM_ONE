package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AllDataResponse  implements Serializable {

    /*"s_no": "161",
    "first_name": "Shekhar Singh Raghuvanshi",
    "mobile_number": "7678038506",
    "alternate_number": "0",
    "email_id": "shekhar.ravilla@gmail.com",
    "requirement_name": "Tranquil CRM",
    "source_id": "1",
    "lead_source": "Facebook",
    "location": "",
    "activity_id": "7",
    "activity_date": "2022-07-20",
    "activity_time": "15:24:00",
    "activity_name": "Nego Followup",
    "lead_pic": "*/

    private String s_no;
    private String first_name;
    private String mobile_number;
    private String alternate_number;
    private String email_id;
    private String requirement_name;
    private String source_id;
    private String lead_source;
    private String activity_id;
    private String activity_date;
    private String activity_time;
    private String activity_name;
    private String location;
    private String lead_pic;

    private String last_remark;
    private String created_date;
    private String alldata;


    public AllDataResponse(String s_no, String first_name, String mobile_number, String alternate_number, String email_id, String requirement_name, String source_id, String lead_source, String activity_id, String activity_date, String activity_time, String activity_name, String location, String lead_pic) {
        this.s_no = s_no;
        this.first_name = first_name;
        this.mobile_number = mobile_number;
        this.alternate_number = alternate_number;
        this.email_id = email_id;
        this.requirement_name = requirement_name;
        this.source_id = source_id;
        this.lead_source = lead_source;
        this.activity_id = activity_id;
        this.activity_date = activity_date;
        this.activity_time = activity_time;
        this.activity_name = activity_name;
        this.location = location;
        this.lead_pic = lead_pic;
    }

    public AllDataResponse() {

    }


    public String getAlldata() {
        return alldata;
    }

    public void setAlldata(String alldata) {
        this.alldata = alldata;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLast_remark() {
        return last_remark;
    }

    public void setLast_remark(String last_remark) {
        this.last_remark = last_remark;
    }

    public String getLead_source() {
        return lead_source;
    }

    public void setLead_source(String lead_source) {
        this.lead_source = lead_source;
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

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getAlternate_number() {
        return alternate_number;
    }

    public void setAlternate_number(String alternate_number) {
        this.alternate_number = alternate_number;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
