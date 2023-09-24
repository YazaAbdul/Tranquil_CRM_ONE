package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class NotesResponse implements Serializable {

    private String post;
    private String created_by;
    private String created_name;
    private String created_date;
    private String profilepic;

    public NotesResponse(String post, String created_by, String created_name, String created_date, String profilepic) {
        this.post = post;
        this.created_by = created_by;
        this.created_name = created_name;
        this.created_date = created_date;
        this.profilepic = profilepic;
    }


    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_name() {
        return created_name;
    }

    public void setCreated_name(String created_name) {
        this.created_name = created_name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
