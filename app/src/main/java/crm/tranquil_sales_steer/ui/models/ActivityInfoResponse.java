package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 03-Aug-18.
 */
public class ActivityInfoResponse {

    private String actvty_typ_nm;
    private String actvty_typ_id;
    private String project_name;
    private String project_visited;
    private String actvty_dt;
    private String autoinc_id;
    private String remarks;
    private String actvty_is_cmpltd;

    public String getActvty_typ_nm() {
        return actvty_typ_nm;
    }

    public void setActvty_typ_nm(String actvty_typ_nm) {
        this.actvty_typ_nm = actvty_typ_nm;
    }

    public String getActvty_typ_id() {
        return actvty_typ_id;
    }

    public void setActvty_typ_id(String actvty_typ_id) {
        this.actvty_typ_id = actvty_typ_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_visited() {
        return project_visited;
    }

    public void setProject_visited(String project_visited) {
        this.project_visited = project_visited;
    }

    public String getActvty_dt() {
        return actvty_dt;
    }

    public void setActvty_dt(String actvty_dt) {
        this.actvty_dt = actvty_dt;
    }

    public String getAutoinc_id() {
        return autoinc_id;
    }

    public void setAutoinc_id(String autoinc_id) {
        this.autoinc_id = autoinc_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getActvty_is_cmpltd() {
        return actvty_is_cmpltd;
    }

    public void setActvty_is_cmpltd(String actvty_is_cmpltd) {
        this.actvty_is_cmpltd = actvty_is_cmpltd;
    }
}
