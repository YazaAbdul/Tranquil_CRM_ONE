package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class UserStatusResponse implements Serializable {

    private Boolean status;
    private String status_msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }
}
