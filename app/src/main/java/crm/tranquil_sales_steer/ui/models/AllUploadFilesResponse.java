package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class AllUploadFilesResponse implements Serializable {

    private String id;
    private String created_date;
    private String file_name;
    private String upload_doc;

    public AllUploadFilesResponse(String id, String created_date, String file_name, String upload_doc) {
        this.id = id;
        this.created_date = created_date;
        this.file_name = file_name;
        this.upload_doc = upload_doc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getUpload_doc() {
        return upload_doc;
    }

    public void setUpload_doc(String upload_doc) {
        this.upload_doc = upload_doc;
    }
}
