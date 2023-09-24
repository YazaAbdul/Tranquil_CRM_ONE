package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class SourceTypesResponse implements Serializable {

    /*"source_id": "1",
    "source_name": "Facebook",
    "count": 206*/

    private String source_id;
    private String source_name;
    private String count;

    public SourceTypesResponse(String source_id, String source_name, String count) {
        this.source_id = source_id;
        this.source_name = source_name;
        this.count = count;
    }

    public SourceTypesResponse() {

    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
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
