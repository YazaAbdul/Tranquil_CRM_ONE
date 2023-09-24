package crm.tranquil_sales_steer.ui.models;

/**
 * Created by venkei on 08-Nov-19.
 */
public class _TodoResponse {

    private long id;
    private String userID;
    private String taskTitle;
    private String taskName;
    private String date;
    private String time;
    private String category;
    private String repeatStr;

    public _TodoResponse(long id,String userID, String taskTitle, String taskName, String date, String time, String category,String repeatStr) {
        this.id=id;
        this.userID = userID;
        this.taskTitle = taskTitle;
        this.taskName = taskName;
        this.date = date;
        this.time = time;
        this.category = category;
        this.repeatStr=repeatStr;
    }

    public _TodoResponse() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRepeatStr() {
        return repeatStr;
    }

    public void setRepeatStr(String repeatStr) {
        this.repeatStr = repeatStr;
    }
}
