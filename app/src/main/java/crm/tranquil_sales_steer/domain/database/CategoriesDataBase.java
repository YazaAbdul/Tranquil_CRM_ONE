package crm.tranquil_sales_steer.domain.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by venkei on 23-Jul-19.
 */
public class CategoriesDataBase extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "create_categories";

    public CategoriesDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CategoriesResponse.CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoriesResponse.CATEGORIES_TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }



    public long insertCategories(String category) {
        // get writable database as we want to write data
        List<CategoriesResponse> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(CategoriesResponse.COLUMN_CATEGORY_NAME, category);

        // insert row
        long id = db.insert(CategoriesResponse.CATEGORIES_TABLE_NAME, null, values);

        // close db connection
        db.close();

        return id;
    }

    public CategoriesResponse getCategory(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CategoriesResponse.CATEGORIES_TABLE_NAME,
                new String[]{CategoriesResponse.COLUMN_ID,
                        CategoriesResponse.COLUMN_CATEGORY_NAME},

                CategoriesResponse.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        @SuppressLint("Range") CategoriesResponse note = new CategoriesResponse(cursor.getInt(cursor.getColumnIndex(CategoriesResponse.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(CategoriesResponse.COLUMN_CATEGORY_NAME)));

        // close the db connection
        cursor.close();

        return note;
    }

    @SuppressLint("Range")
    public List<CategoriesResponse> getAllNotes(Activity activity) {
        List<CategoriesResponse> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CategoriesResponse.CATEGORIES_TABLE_NAME + " ORDER BY " +
                CategoriesResponse.COLUMN_CATEGORY_NAME + " DESC" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                CategoriesResponse note = new CategoriesResponse();

                note.setCat_id(cursor.getInt(cursor.getColumnIndex(CategoriesResponse.COLUMN_ID)));
                note.setCat_title(cursor.getString(cursor.getColumnIndex(CategoriesResponse.COLUMN_CATEGORY_NAME)));
                notes.add(note);


            } while (cursor.moveToNext());
        }



        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public void deleteNote(CategoriesResponse note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CategoriesResponse.CATEGORIES_TABLE_NAME, CategoriesResponse.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getCat_id())});
        db.close();
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + CategoriesResponse.CATEGORIES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public class CategoriesResponse {

        public static final String CATEGORIES_TABLE_NAME = "to_do_categories";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY_NAME = "category_title";
        public static final String COLUMN_TIMESTAMP="timestamp";


        public static final String CREATE_CATEGORIES_TABLE="CREATE TABLE " + CATEGORIES_TABLE_NAME
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_CATEGORY_NAME+ " TEXT"
                + ")";



        private int cat_id;
        private String cat_title;

        public CategoriesResponse(int cat_id, String cat_title) {
            this.cat_id = cat_id;
            this.cat_title = cat_title;
        }

        public CategoriesResponse() {

        }

        public int getCat_id() {
            return cat_id;
        }

        public void setCat_id(int cat_id) {
            this.cat_id = cat_id;
        }

        public String getCat_title() {
            return cat_title;
        }

        public void setCat_title(String cat_title) {
            this.cat_title = cat_title;
        }
    }
}
