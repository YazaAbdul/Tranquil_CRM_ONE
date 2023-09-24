package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 16-Jun-18.
 */

public class MeetSubmitionRespons {
    private String site_id,activitystatus;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getActivitystatus() {
        return activitystatus;
    }

    public void setActivitystatus(String activitystatus) {
        this.activitystatus = activitystatus;
    }

    public MeetSubmitionRespons(String site_id, String activitystatus) {
        this.site_id = site_id;
        this.activitystatus = activitystatus;
    }
}
