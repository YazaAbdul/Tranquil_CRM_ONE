package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

/**
 * Created by venkei on 19-Mar-19.
 */
public class DashboardChildResponse implements Serializable {

    /*actvty_typ_id: "2",
actvty_typ_nm: "Phone Call",
act_image: "http://www.tranquilrealestatesoftware.in/actimages/new_phone_call.png",
count: 6*/

    private String actvty_typ_id;
    private String actvty_typ_nm;
    private String act_image;
    private String count;

    public DashboardChildResponse(String actvty_typ_id, String actvty_typ_nm, String act_image, String count) {
        this.actvty_typ_id = actvty_typ_id;
        this.actvty_typ_nm = actvty_typ_nm;
        this.act_image = act_image;
        this.count = count;
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

    public String getAct_image() {
        return act_image;
    }

    public void setAct_image(String act_image) {
        this.act_image = act_image;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
