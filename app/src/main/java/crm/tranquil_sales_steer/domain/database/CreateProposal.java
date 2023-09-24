package crm.tranquil_sales_steer.domain.database;

/**
 * Created by venkei on 18-Jun-19.
 */
public class CreateProposal {

    public static final String TABLE_NAME = "create_price_proposal";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "product_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_GST = "tax";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String product_name;
    private String quantity;
    private String price;
    private String tax;
    private String timestamp;


    public static final String CREATE_TABLE="CREATE TABLE " + TABLE_NAME
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COLUMN_PRODUCT_NAME+ " TEXT,"
            +COLUMN_QUANTITY + " TEXT,"
            +COLUMN_PRICE + " TEXT,"
            +COLUMN_GST + " TEXT,"
            +COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";




    public CreateProposal(){

    }

    public CreateProposal(int id, String product_name, String quantity, String price, String tax, String timestamp) {
        this.id = id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
        this.tax = tax;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
