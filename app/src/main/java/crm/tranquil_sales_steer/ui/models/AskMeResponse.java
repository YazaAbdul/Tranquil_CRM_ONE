package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;
import java.util.ArrayList;

public class AskMeResponse implements Serializable {

    private String id;
    private String object;
    private String created;
    private String model;
    private ArrayList<AskMeChoisesResponse> choices = new ArrayList<>();

    private AshMeUsageResponse usage;

    public AshMeUsageResponse getUsage() {
        return usage;
    }

    public void setUsage(AshMeUsageResponse usage) {
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<AskMeChoisesResponse> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<AskMeChoisesResponse> choices) {
        this.choices = choices;
    }
}
