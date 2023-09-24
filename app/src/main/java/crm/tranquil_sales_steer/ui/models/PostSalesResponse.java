package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class PostSalesResponse implements Serializable {

    private String id;
    private String name;
    private String color_start;
    private String color_end;
    private String color_light;
    private String color_light2;
    private int image;

    public PostSalesResponse(String id, String name, String color_start, String color_end, String color_light, String color_light2, int image) {
        this.id = id;
        this.name = name;
        this.color_start = color_start;
        this.color_end = color_end;
        this.color_light = color_light;
        this.color_light2 = color_light2;
        this.image = image;
    }


    public String getColor_start() {
        return color_start;
    }

    public void setColor_start(String color_start) {
        this.color_start = color_start;
    }

    public String getColor_end() {
        return color_end;
    }

    public void setColor_end(String color_end) {
        this.color_end = color_end;
    }

    public String getColor_light() {
        return color_light;
    }

    public void setColor_light(String color_light) {
        this.color_light = color_light;
    }

    public String getColor_light2() {
        return color_light2;
    }

    public void setColor_light2(String color_light2) {
        this.color_light2 = color_light2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
