package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 06-Jul-19.
 */
public class SourceResponse {
    /*source_id: "1",
source_name: "Facebook"*/

    private String source_id;
    private String source_name;

    public SourceResponse(String source_id, String source_name) {
        this.source_id = source_id;
        this.source_name = source_name;
    }

    public SourceResponse() {

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
}
