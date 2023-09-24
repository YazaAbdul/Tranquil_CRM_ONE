package crm.tranquil_sales_steer.ui.models;

/**
 * Created by chandu on 01-feb-2022.
 */
public class PunchStatusResponse {
    private String punch_date;
    private String punch_intime;
    private String punch_outtime;
    private String location;
    private String area;
    private String outlocation;
    private String outarea;
    private String punchout_date;

    public PunchStatusResponse(String punch_date, String punch_intime, String punch_outtime, String location, String area, String outlocation, String outarea, String punchout_date) {
        this.punch_date = punch_date;
        this.punch_intime = punch_intime;
        this.punch_outtime = punch_outtime;
        this.location = location;
        this.area = area;
        this.outlocation = outlocation;
        this.outarea = outarea;
        this.punchout_date = punchout_date;
    }

    public String getPunch_date() {
        return punch_date;
    }

    public void setPunch_date(String punch_date) {
        this.punch_date = punch_date;
    }

    public String getPunch_intime() {
        return punch_intime;
    }

    public void setPunch_intime(String punch_intime) {
        this.punch_intime = punch_intime;
    }

    public String getPunch_outtime() {
        return punch_outtime;
    }

    public void setPunch_outtime(String punch_outtime) {
        this.punch_outtime = punch_outtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOutlocation() {
        return outlocation;
    }

    public void setOutlocation(String outlocation) {
        this.outlocation = outlocation;
    }

    public String getOutarea() {
        return outarea;
    }

    public void setOutarea(String outarea) {
        this.outarea = outarea;
    }

    public String getPunchout_date() {
        return punchout_date;
    }

    public void setPunchout_date(String punchout_date) {
        this.punchout_date = punchout_date;
    }
}
