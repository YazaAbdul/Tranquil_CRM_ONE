package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class DashBoardResponseNew implements Serializable {

    private String activity_id;
    private String activity_name;
    private String color_one;
    private String color_two;
    private String activity_image;

    private String activity_count;

    public DashBoardResponseNew(String activity_id, String activity_name, String color_one, String color_two, String color_light, String color_light_two,String activity_image, String activity_count) {
        this.activity_id = activity_id;
        this.activity_name = activity_name;
        this.color_one = color_one;
        this.color_two = color_two;
        this.activity_image=activity_image;

        this.activity_count = activity_count;
    }


    public String getActivity_image() {
       return activity_image;
    }

    public void setActivity_image(String activity_image) {
        this.activity_image = activity_image;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public String getColor_one() {
        return color_one;
    }

    public void setColor_one(String color_one) {
        this.color_one = color_one;
    }

    public String getColor_two() {
        return color_two;
    }

    public void setColor_two(String color_two) {
        this.color_two = color_two;
    }

    public String getActivity_count() {
        return activity_count;
    }

    public void setActivity_count(String activity_count) {
        this.activity_count = activity_count;
    }


}
