package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class CommunicationsResponse implements Serializable {

    private String call_id;
    private String status;

    public CommunicationsResponse(String call_id, String status) {
        this.call_id = call_id;
        this.status = status;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
