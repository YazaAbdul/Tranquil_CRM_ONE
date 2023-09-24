package crm.tranquil_sales_steer.ui.models;

import java.io.Serializable;

public class CardsResponse implements Serializable {

    private String card_id;
    private String card_name;
    private String p1;
    private String p2;
    private String p3;
    private String p1logic;
    private String p2logic;
    private String p3logic;
    private String color_one;
    private String card_image;


    private String icons;

    public CardsResponse(String card_id, String card_name, String p1, String p2, String p3, String p1logic, String p2logic, String p3logic, String color_one, String card_image, String icons) {
        this.card_id = card_id;
        this.card_name = card_name;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p1logic = p1logic;
        this.p2logic = p2logic;
        this.p3logic = p3logic;
        this.color_one = color_one;
this.card_image=card_image;
        this.icons = icons;
    }

    public String getColor_one() {
        return color_one;
    }

    public void setColor_one(String color_one) {
        this.color_one = color_one;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP1logic() {
        return p1logic;
    }

    public void setP1logic(String p1logic) {
        this.p1logic = p1logic;
    }

    public String getP2logic() {
        return p2logic;
    }

    public void setP2logic(String p2logic) {
        this.p2logic = p2logic;
    }

    public String getP3logic() {
        return p3logic;
    }

    public void setP3logic(String p3logic) {
        this.p3logic = p3logic;
    }
}
