package crm.tranquil_sales_steer.domain.database;

/**
 * Created by Juned on 1/23/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

   public static String DATABASE_NAME="menu.bd";

    public static final String TABLE_NAME="MenuTable";

    public static final String Table_Column_ID="id";

    public static final String menu_id="menuId";

    public static final String menu_title="menuTitle";

    public static final String menu_icon="menuIcon";

    public static final String menu_url="menuUrl";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+Table_Column_ID+" INTEGER PRIMARY KEY, "+menu_id+" VARCHAR, "+menu_title+" VARCHAR, "+menu_icon+" VARCHAR,"+menu_url+" VARCHAR)";
        database.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

}