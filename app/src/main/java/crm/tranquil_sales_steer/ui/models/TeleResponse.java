package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class TeleResponse implements Serializable {

    private String msg;
    private String tele_msg;

    public TeleResponse(String msg, String tele_msg) {
        this.msg = msg;
        this.tele_msg = tele_msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTele_msg() {
        return tele_msg;
    }

    public void setTele_msg(String tele_msg) {
        this.tele_msg = tele_msg;
    }
}
