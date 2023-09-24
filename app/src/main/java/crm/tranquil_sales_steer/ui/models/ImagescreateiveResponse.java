package crm.tranquil_sales_steer.ui.models;

public class ImagescreateiveResponse {


    String s_no;
    String file_name;
    String category;
    String status;


    public ImagescreateiveResponse(String s_no, String file_name, String category, String status) {
        this.s_no = s_no;
        this.file_name = file_name;
        this.category = category;
        this.status = status;
    }

    public ImagescreateiveResponse() {

    }


    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
