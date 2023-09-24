package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

/**
 * Created by venkei on 14-Nov-19.
 */
public class TelecallersResponse implements Serializable {

    /*msg: "Mobile Number Already Exist",
status: 0*/

    private String msg;
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
