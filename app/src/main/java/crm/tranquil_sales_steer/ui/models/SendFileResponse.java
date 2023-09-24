package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class SendFileResponse implements Serializable {

    private String status;
    private String message;

    public SendFileResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
