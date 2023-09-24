package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

/**
 * Created by Chandu on 05-Sep-2022.
 */
public class DashboardResponse implements Serializable {

    private String user_id;
    private String user_name;
    private String user_type;
    private String reporting_to;
    private String designation;
    private String user_role;
    private String user_profile_pic;

    private String href_status;
    private String dialed_count;
    private String dialed_count_monthly;
    private String dialed_count_overall;
    private String spoken_count;
    private String spoken_count_mnth;
    private String spoken_count_overall;
    private String talktime_count;
    private String talktime_count_mnth;
    private String talktime_count_overall;
    private String meets_count;
    private String meets_count_mnth;
    private String meets_count_overall;
    private String enq_count;
    private String enq_count_mnth;
    private String enq_count_overall;
    private String dropped_count;
    private String dropped_count_mnth;
    private String dropped_count_overall;
    private String meet_sch_count;
    private String meet_sch_count_mnth;
    private String meet_sch_count_overall;
    private String meet_complt_count;
    private String meet_complt_count_mnth;
    private String meet_complt_count_overall;
    private String sale_sch_count;
    private String sale_sch_count_mnth;
    private String sale_sch_count_overall;
    private String sale_complt_count;
    private String sale_complt_count_mnth;
    private String sale_complt_count_overall;

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTalktime_count() {
        return talktime_count;
    }

    public void setTalktime_count(String talktime_count) {
        this.talktime_count = talktime_count;
    }

    public String getTalktime_count_mnth() {
        return talktime_count_mnth;
    }

    public void setTalktime_count_mnth(String talktime_count_mnth) {
        this.talktime_count_mnth = talktime_count_mnth;
    }

    public String getTalktime_count_overall() {
        return talktime_count_overall;
    }

    public void setTalktime_count_overall(String talktime_count_overall) {
        this.talktime_count_overall = talktime_count_overall;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getReporting_to() {
        return reporting_to;
    }

    public void setReporting_to(String reporting_to) {
        this.reporting_to = reporting_to;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getHref_status() {
        return href_status;
    }

    public void setHref_status(String href_status) {
        this.href_status = href_status;
    }

    public String getDialed_count() {
        return dialed_count;
    }

    public void setDialed_count(String dialed_count) {
        this.dialed_count = dialed_count;
    }

    public String getDialed_count_monthly() {
        return dialed_count_monthly;
    }

    public void setDialed_count_monthly(String dialed_count_monthly) {
        this.dialed_count_monthly = dialed_count_monthly;
    }

    public String getDialed_count_overall() {
        return dialed_count_overall;
    }

    public void setDialed_count_overall(String dialed_count_overall) {
        this.dialed_count_overall = dialed_count_overall;
    }

    public String getSpoken_count() {
        return spoken_count;
    }

    public void setSpoken_count(String spoken_count) {
        this.spoken_count = spoken_count;
    }

    public String getSpoken_count_mnth() {
        return spoken_count_mnth;
    }

    public void setSpoken_count_mnth(String spoken_count_mnth) {
        this.spoken_count_mnth = spoken_count_mnth;
    }

    public String getSpoken_count_overall() {
        return spoken_count_overall;
    }

    public void setSpoken_count_overall(String spoken_count_overall) {
        this.spoken_count_overall = spoken_count_overall;
    }

    public String getMeets_count() {
        return meets_count;
    }

    public void setMeets_count(String meets_count) {
        this.meets_count = meets_count;
    }

    public String getMeets_count_mnth() {
        return meets_count_mnth;
    }

    public void setMeets_count_mnth(String meets_count_mnth) {
        this.meets_count_mnth = meets_count_mnth;
    }

    public String getMeets_count_overall() {
        return meets_count_overall;
    }

    public void setMeets_count_overall(String meets_count_overall) {
        this.meets_count_overall = meets_count_overall;
    }

    public String getEnq_count() {
        return enq_count;
    }

    public void setEnq_count(String enq_count) {
        this.enq_count = enq_count;
    }

    public String getEnq_count_mnth() {
        return enq_count_mnth;
    }

    public void setEnq_count_mnth(String enq_count_mnth) {
        this.enq_count_mnth = enq_count_mnth;
    }

    public String getEnq_count_overall() {
        return enq_count_overall;
    }

    public void setEnq_count_overall(String enq_count_overall) {
        this.enq_count_overall = enq_count_overall;
    }

    public String getDropped_count() {
        return dropped_count;
    }

    public void setDropped_count(String dropped_count) {
        this.dropped_count = dropped_count;
    }

    public String getDropped_count_mnth() {
        return dropped_count_mnth;
    }

    public void setDropped_count_mnth(String dropped_count_mnth) {
        this.dropped_count_mnth = dropped_count_mnth;
    }

    public String getDropped_count_overall() {
        return dropped_count_overall;
    }

    public void setDropped_count_overall(String dropped_count_overall) {
        this.dropped_count_overall = dropped_count_overall;
    }

    public String getMeet_sch_count() {
        return meet_sch_count;
    }

    public void setMeet_sch_count(String meet_sch_count) {
        this.meet_sch_count = meet_sch_count;
    }

    public String getMeet_sch_count_mnth() {
        return meet_sch_count_mnth;
    }

    public void setMeet_sch_count_mnth(String meet_sch_count_mnth) {
        this.meet_sch_count_mnth = meet_sch_count_mnth;
    }

    public String getMeet_sch_count_overall() {
        return meet_sch_count_overall;
    }

    public void setMeet_sch_count_overall(String meet_sch_count_overall) {
        this.meet_sch_count_overall = meet_sch_count_overall;
    }

    public String getMeet_complt_count() {
        return meet_complt_count;
    }

    public void setMeet_complt_count(String meet_complt_count) {
        this.meet_complt_count = meet_complt_count;
    }

    public String getMeet_complt_count_mnth() {
        return meet_complt_count_mnth;
    }

    public void setMeet_complt_count_mnth(String meet_complt_count_mnth) {
        this.meet_complt_count_mnth = meet_complt_count_mnth;
    }

    public String getMeet_complt_count_overall() {
        return meet_complt_count_overall;
    }

    public void setMeet_complt_count_overall(String meet_complt_count_overall) {
        this.meet_complt_count_overall = meet_complt_count_overall;
    }

    public String getSale_sch_count() {
        return sale_sch_count;
    }

    public void setSale_sch_count(String sale_sch_count) {
        this.sale_sch_count = sale_sch_count;
    }

    public String getSale_sch_count_mnth() {
        return sale_sch_count_mnth;
    }

    public void setSale_sch_count_mnth(String sale_sch_count_mnth) {
        this.sale_sch_count_mnth = sale_sch_count_mnth;
    }

    public String getSale_sch_count_overall() {
        return sale_sch_count_overall;
    }

    public void setSale_sch_count_overall(String sale_sch_count_overall) {
        this.sale_sch_count_overall = sale_sch_count_overall;
    }

    public String getSale_complt_count() {
        return sale_complt_count;
    }

    public void setSale_complt_count(String sale_complt_count) {
        this.sale_complt_count = sale_complt_count;
    }

    public String getSale_complt_count_mnth() {
        return sale_complt_count_mnth;
    }

    public void setSale_complt_count_mnth(String sale_complt_count_mnth) {
        this.sale_complt_count_mnth = sale_complt_count_mnth;
    }

    public String getSale_complt_count_overall() {
        return sale_complt_count_overall;
    }

    public void setSale_complt_count_overall(String sale_complt_count_overall) {
        this.sale_complt_count_overall = sale_complt_count_overall;
    }
}
