package crm.tranquil_sales_steer.ui.models;

public class HolidaysResponse {
    /*holidaydate: "1 Jan 2018",
holidayday: "Monday",
holidayname: "NEW YEAR "*/

    private String holidaydate;
    private String holidayday;
    private String holidayname;

    public String getHolidaydate() {
        return holidaydate;
    }

    public void setHolidaydate(String holidaydate) {
        this.holidaydate = holidaydate;
    }

    public String getHolidayday() {
        return holidayday;
    }

    public void setHolidayday(String holidayday) {
        this.holidayday = holidayday;
    }

    public String getHolidayname() {
        return holidayname;
    }

    public void setHolidayname(String holidayname) {
        this.holidayname = holidayname;
    }
}
