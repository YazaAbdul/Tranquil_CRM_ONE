package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class FoldersResponse implements Serializable {

    private String folder_id;
    private String folder_title;

    public FoldersResponse(String folder_id, String folder_title) {
        this.folder_id = folder_id;
        this.folder_title = folder_title;
    }

    public String getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(String folder_id) {
        this.folder_id = folder_id;
    }

    public String getFolder_title() {
        return folder_title;
    }

    public void setFolder_title(String folder_title) {
        this.folder_title = folder_title;
    }
}
