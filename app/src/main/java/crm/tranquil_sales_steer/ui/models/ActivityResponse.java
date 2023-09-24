package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 04-Mar-19.
 */
public class ActivityResponse {

    /*actvty_typ_id: "2",
actvty_typ_nm: "Phone Call"*/

    private String actvty_typ_id;
    private String actvty_typ_nm;

    public ActivityResponse(String actvty_typ_id, String actvty_typ_nm) {
        this.actvty_typ_id = actvty_typ_id;
        this.actvty_typ_nm = actvty_typ_nm;
    }

    public String getActvty_typ_id() {
        return actvty_typ_id;
    }

    public void setActvty_typ_id(String actvty_typ_id) {
        this.actvty_typ_id = actvty_typ_id;
    }

    public String getActvty_typ_nm() {
        return actvty_typ_nm;
    }

    public void setActvty_typ_nm(String actvty_typ_nm) {
        this.actvty_typ_nm = actvty_typ_nm;
    }
}
