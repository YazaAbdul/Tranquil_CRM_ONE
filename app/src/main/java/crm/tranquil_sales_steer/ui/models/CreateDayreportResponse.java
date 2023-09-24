package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class CreateDayreportResponse implements Serializable {


    String activity_id;
    String activity_name;
    String daily_tgt;
    String user_id;
    String count;

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

    public String getDaily_tgt() {
        return daily_tgt;
    }

    public void setDaily_tgt(String daily_tgt) {
        this.daily_tgt = daily_tgt;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
