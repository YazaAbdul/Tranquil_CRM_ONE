package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 05-Jun-18.
 */

public class SearchResponse {
    private String customer_name,mobile,customer_id;

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public SearchResponse(String customer_name, String mobile, String customer_id) {
        this.customer_name = customer_name;
        this.mobile = mobile;
        this.customer_id=customer_id;
    }
}
