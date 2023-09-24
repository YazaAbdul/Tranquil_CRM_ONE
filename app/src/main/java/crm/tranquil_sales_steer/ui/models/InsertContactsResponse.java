package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class InsertContactsResponse implements Serializable {

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
