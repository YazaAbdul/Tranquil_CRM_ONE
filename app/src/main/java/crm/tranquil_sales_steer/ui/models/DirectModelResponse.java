package crm.tranquil_sales_steer.ui.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectModelResponse {

    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}