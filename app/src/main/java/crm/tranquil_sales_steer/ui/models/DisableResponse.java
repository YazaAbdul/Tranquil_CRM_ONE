package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 06-Jul-19.
 */
public class DisableResponse {

    private String reason_id;
    private String dead_reason;

    public DisableResponse() {
    }

    public DisableResponse(String reason_id, String dead_reason) {
        this.reason_id = reason_id;
        this.dead_reason = dead_reason;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getDead_reason() {
        return dead_reason;
    }

    public void setDead_reason(String dead_reason) {
        this.dead_reason = dead_reason;
    }
}
