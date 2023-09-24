package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class CreativesResponse implements Serializable {

    /*s_no": "1",
    "image":*/

    private String s_no;
    private String image;

    public CreativesResponse(String s_no, String image) {
        this.s_no = s_no;
        this.image = image;
    }

    public CreativesResponse() {

    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
