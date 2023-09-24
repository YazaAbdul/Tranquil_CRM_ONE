package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class TeleSourceTypeResponse implements Serializable {

    /*"telecaller_source_id": "1",
    "source_name": "BUILDER",
    "count": 0*/

    private String telecaller_source_id;
    private String source_name;
    private String count;

    public TeleSourceTypeResponse(String telecaller_source_id, String source_name, String count) {
        this.telecaller_source_id = telecaller_source_id;
        this.source_name = source_name;
        this.count = count;
    }

    public String getTelecaller_source_id() {
        return telecaller_source_id;
    }

    public void setTelecaller_source_id(String telecaller_source_id) {
        this.telecaller_source_id = telecaller_source_id;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
