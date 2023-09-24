package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class PdfModel implements Serializable {

    private String message;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
