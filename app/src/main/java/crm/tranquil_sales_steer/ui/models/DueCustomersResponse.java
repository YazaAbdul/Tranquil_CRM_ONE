package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class DueCustomersResponse implements Serializable {

    private String project_id;
    private String project_name;
    private int sold;
    private int total_sale_amt;
    private int received;
    private int due_amount;

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getTotal_sale_amt() {
        return total_sale_amt;
    }

    public void setTotal_sale_amt(int total_sale_amt) {
        this.total_sale_amt = total_sale_amt;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(int due_amount) {
        this.due_amount = due_amount;
    }
}
