package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class SaleDoneResponse implements Serializable {

    private String customer_id;
    private String customer_name;
    private String project_name;
    private String unit_number;
    private String sale_amt;

    public SaleDoneResponse(String customer_id, String customer_name, String project_name, String unit_number, String sale_amt) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.project_name = project_name;
        this.unit_number = unit_number;
        this.sale_amt = sale_amt;
    }

    public SaleDoneResponse() {

    }

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

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getUnit_number() {
        return unit_number;
    }

    public void setUnit_number(String unit_number) {
        this.unit_number = unit_number;
    }

    public String getSale_amt() {
        return sale_amt;
    }

    public void setSale_amt(String sale_amt) {
        this.sale_amt = sale_amt;
    }
}
