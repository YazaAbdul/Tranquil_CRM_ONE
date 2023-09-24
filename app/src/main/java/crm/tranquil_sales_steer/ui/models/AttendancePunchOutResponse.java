package crm.tranquil_sales_steer.ui.models;

/**
 * Created by chandcu on 01-feb-2022.
 */
public class AttendancePunchOutResponse {
    private String punchiostatus;
    private String status;
    private String texmsg;

    public AttendancePunchOutResponse(String punchiostatus, String status, String texmsg) {
        this.punchiostatus = punchiostatus;
        this.status = status;
        this.texmsg = texmsg;
    }

    public String getPunchiostatus() {
        return punchiostatus;
    }

    public void setPunchiostatus(String punchiostatus) {
        this.punchiostatus = punchiostatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTexmsg() {
        return texmsg;
    }

    public void setTexmsg(String texmsg) {
        this.texmsg = texmsg;
    }
}
