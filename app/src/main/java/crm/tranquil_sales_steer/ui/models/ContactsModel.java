package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;
import java.util.Objects;

public class ContactsModel implements Serializable {

    private String name;
    private String number;

    public ContactsModel(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContactsModel other = (ContactsModel) obj;
        if (!Objects.equals(this.number, other.number)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.number);
        return hash;
    }

}
