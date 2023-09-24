package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 16-Jun-18.
 */

public class SiteVisitStartResponse {

    private String updateid,activitystatus;

    public String getUpdateid() {
        return updateid;
    }

    public void setUpdateid(String updateid) {
        this.updateid = updateid;
    }

    public String getActivitystatus() {
        return activitystatus;
    }

    public void setActivitystatus(String activitystatus) {
        this.activitystatus = activitystatus;
    }

    public SiteVisitStartResponse(String updateid, String activitystatus) {
        this.updateid = updateid;
        this.activitystatus = activitystatus;
    }
}
