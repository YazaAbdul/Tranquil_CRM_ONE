package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class DueCustomersListResponse implements Serializable {

    private String customer_id;
    private String customer_name;
    private String unit_number;
    private String total_unit_cost;
    private String received_amt;
    private int due_amt;

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getUnit_number() {
        return unit_number;
    }

    public void setUnit_number(String unit_number) {
        this.unit_number = unit_number;
    }

    public String getTotal_unit_cost() {
        return total_unit_cost;
    }

    public void setTotal_unit_cost(String total_unit_cost) {
        this.total_unit_cost = total_unit_cost;
    }

    public String getReceived_amt() {
        return received_amt;
    }

    public void setReceived_amt(String received_amt) {
        this.received_amt = received_amt;
    }

    public int getDue_amt() {
        return due_amt;
    }

    public void setDue_amt(int due_amt) {
        this.due_amt = due_amt;
    }
}
