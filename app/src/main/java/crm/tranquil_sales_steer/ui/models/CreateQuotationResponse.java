package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateQuotationResponse implements Serializable {

    private ArrayList<AddQuotationResponse> quotationResponses = new ArrayList<>();
    private  ArrayList<AddQuotationTotalResponse> quotationTotalResponses = new ArrayList<>();

    public ArrayList<AddQuotationResponse> getQuotationResponses() {
        return quotationResponses;
    }

    public void setQuotationResponses(ArrayList<AddQuotationResponse> quotationResponses) {
        this.quotationResponses = quotationResponses;
    }

    public ArrayList<AddQuotationTotalResponse> getQuotationTotalResponses() {
        return quotationTotalResponses;
    }

    public void setQuotationTotalResponses(ArrayList<AddQuotationTotalResponse> quotationTotalResponses) {
        this.quotationTotalResponses = quotationTotalResponses;
    }
}
