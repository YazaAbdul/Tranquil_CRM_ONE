package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class CampaignCallResponse implements Serializable {

    private Boolean status;
    private String msg;

    public CampaignCallResponse(Boolean status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
