package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardProfileResponse implements Serializable {

    private DashboardResponse dashboard;
    private ArrayList<ReportingUsersResponse> reporting_users = new ArrayList<>();
    private ArrayList<CallHistoryResponse> call_history = new ArrayList<>();

    public DashboardProfileResponse(DashboardResponse dashboard, ArrayList<ReportingUsersResponse> reporting_users, ArrayList<CallHistoryResponse> call_history) {
        this.dashboard = dashboard;
        this.reporting_users = reporting_users;
        this.call_history = call_history;
    }

    public DashboardResponse getDashboard() {
        return dashboard;
    }

    public void setDashboard(DashboardResponse dashboard) {
        this.dashboard = dashboard;
    }

    public ArrayList<ReportingUsersResponse> getReporting_users() {
        return reporting_users;
    }

    public void setReporting_users(ArrayList<ReportingUsersResponse> reporting_users) {
        this.reporting_users = reporting_users;
    }

    public ArrayList<CallHistoryResponse> getCall_history() {
        return call_history;
    }

    public void setCall_history(ArrayList<CallHistoryResponse> call_history) {
        this.call_history = call_history;
    }
}
