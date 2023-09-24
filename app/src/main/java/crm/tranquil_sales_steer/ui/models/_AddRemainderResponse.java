package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 11-Nov-19.
 */
public class _AddRemainderResponse {

    private int remainderID;
    private String remainderActivity;
    private String customerID;
    private String userID;
    private String time;
    private String date;
    private String repeat;
    private String typeOfRepeat;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String customerCompanyName;
    private String customerPicUrl;

    public _AddRemainderResponse(){

    }


    public _AddRemainderResponse(int remainderID, String remainderActivity, String customerID, String userID, String time, String date, String repeat, String typeOfRepeat, String customerName, String customerMobile, String customerEmail, String customerCompanyName, String customerPicUrl) {
        this.remainderID = remainderID;
        this.remainderActivity = remainderActivity;
        this.customerID = customerID;
        this.userID = userID;
        this.time = time;
        this.date = date;
        this.repeat = repeat;
        this.typeOfRepeat = typeOfRepeat;

        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerEmail = customerEmail;
        this.customerCompanyName = customerCompanyName;
        this.customerPicUrl = customerPicUrl;
    }

    public int getRemainderID() {
        return remainderID;
    }

    public void setRemainderID(int remainderID) {
        this.remainderID = remainderID;
    }

    public String getRemainderActivity() {
        return remainderActivity;
    }

    public void setRemainderActivity(String remainderActivity) {
        this.remainderActivity = remainderActivity;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getTypeOfRepeat() {
        return typeOfRepeat;
    }

    public void setTypeOfRepeat(String typeOfRepeat) {
        this.typeOfRepeat = typeOfRepeat;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerCompanyName() {
        return customerCompanyName;
    }

    public void setCustomerCompanyName(String customerCompanyName) {
        this.customerCompanyName = customerCompanyName;
    }

    public String getCustomerPicUrl() {
        return customerPicUrl;
    }

    public void setCustomerPicUrl(String customerPicUrl) {
        this.customerPicUrl = customerPicUrl;
    }
}
