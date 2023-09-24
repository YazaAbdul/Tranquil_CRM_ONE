package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AllActivityResponse implements Serializable {

    /*activity_id": "1",
    "activity_name": "Yet to Contact",
    "count": 4623*/

    private String activity_id;
    private String activity_name;
    private String count;

    public AllActivityResponse(String activity_id, String activity_name, String count) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.count = count;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
