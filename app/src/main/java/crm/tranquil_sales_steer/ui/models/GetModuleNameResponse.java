package crm.tranquil_sales_steer.ui.models;

public class GetModuleNameResponse {
    String m_id;
    String name;


    public GetModuleNameResponse(String m_id, String name) {
        this.m_id = m_id;
        this.name = name;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
