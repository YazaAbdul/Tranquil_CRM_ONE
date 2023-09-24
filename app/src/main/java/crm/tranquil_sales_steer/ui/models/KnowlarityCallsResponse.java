package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class KnowlarityCallsResponse implements Serializable {

    private String call_id;
    private String mobile_number;
    private String customer_name;
    private String destination;
    private String agent_status;
    private String duration;
    private String call_end;
    private String audio_file;

    public KnowlarityCallsResponse(String call_id, String mobile_number, String customer_name, String destination, String agent_status, String duration, String call_end, String audio_file) {
        this.call_id = call_id;
        this.mobile_number = mobile_number;
        this.customer_name = customer_name;
        this.destination = destination;
        this.agent_status = agent_status;
        this.duration = duration;
        this.call_end = call_end;
        this.audio_file = audio_file;
    }

    public KnowlarityCallsResponse() {

    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCall_end() {
        return call_end;
    }

    public void setCall_end(String call_end) {
        this.call_end = call_end;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAgent_status() {
        return agent_status;
    }

    public void setAgent_status(String agent_status) {
        this.agent_status = agent_status;
    }
}
