package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactsRequest implements Serializable {

    private ArrayList<String> contactName;
    private ArrayList<String> contactNumber;

    public ContactsRequest(ArrayList<String> contactName, ArrayList<String> contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }
}
