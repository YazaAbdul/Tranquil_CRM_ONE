package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

/**
 * Created by venkei on 19-Nov-19.
 */
public class RecordingsList implements Serializable {

    private String lead_id;
    private String lead_name;
    private String user_id;
    private String user_name;
    private String sound_file;
    private String duration;
    private String created_date;

    public RecordingsList(String lead_id, String lead_name, String user_id, String user_name, String sound_file, String duration, String created_date) {
        this.lead_id = lead_id;
        this.lead_name = lead_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.sound_file = sound_file;
        this.duration = duration;
        this.created_date = created_date;
    }

    public RecordingsList() {

    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getLead_name() {
        return lead_name;
    }

    public void setLead_name(String lead_name) {
        this.lead_name = lead_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSound_file() {
        return sound_file;
    }

    public void setSound_file(String sound_file) {
        this.sound_file = sound_file;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
