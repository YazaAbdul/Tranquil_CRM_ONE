package crm.tranquil_sales_steer.domain.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venkei on 24-Jul-19.
 */
public class TaskDataBase extends SQLiteOpenHelper {


    // Database Version
    private static final int NEW_DATABASE_VERSION = 1;

    // Database Name
    private static final String NEW_DATABASE_NAME = "create_task";

    public TaskDataBase(Context context) {
        super(context, NEW_DATABASE_NAME, null, NEW_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TasksResponse.CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TasksResponse.TASK_TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }


    public long insertTasks(String title,String name,String date,String time,String repeat,String category) {
        // get writable database as we want to write data
        List<TasksResponse> notes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(TasksResponse.TASK_COLUMN_TITTLE, title);
        values.put(TasksResponse.TASK_COLUMN_NAME, name);
        values.put(TasksResponse.TASK_COLUMN_DATE, date);
        values.put(TasksResponse.TASK_COLUMN_TIME, time);
        values.put(TasksResponse.TASK_COLUMN_REPEAT, repeat);
        values.put(TasksResponse.TASK_COLUMN_CATEGORY, category);

        // insert row
        long id = db.insert(TasksResponse.TASK_TABLE_NAME, null, values);

        // close db connection
        db.close();

        return id;
    }

    public TasksResponse getTasks(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TasksResponse.TASK_TABLE_NAME,
                new String[]{TasksResponse.TASK_COLUMN_ID,
                        TasksResponse.TASK_COLUMN_TITTLE,
                        TasksResponse.TASK_COLUMN_NAME,
                        TasksResponse.TASK_COLUMN_DATE,
                        TasksResponse.TASK_COLUMN_TIME,
                        TasksResponse.TASK_COLUMN_REPEAT,
                        TasksResponse.TASK_COLUMN_CATEGORY},

                TasksResponse.TASK_COLUMN_ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        TasksResponse note = new TasksResponse(cursor.getInt(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_TITTLE)),
                cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_REPEAT)),
                cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_CATEGORY)));

        // close the db connection
        cursor.close();

        return note;
    }


    public List<TasksResponse> getAllTasks() {
        List<TasksResponse> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TasksResponse.TASK_TABLE_NAME + " ORDER BY " +
                TasksResponse.TASK_COLUMN_TITTLE + " DESC"  ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                TasksResponse note = new TasksResponse();

                note.setTask_id(cursor.getInt(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_ID)));
                note.setTask_title(cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_TITTLE)));
                note.setTask_name(cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_NAME)));
                note.setTask_date(cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_DATE)));
                note.setTask_time(cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_TIME)));
                note.setTask_repeat(cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_REPEAT)));
                note.setTask_category(cursor.getString(cursor.getColumnIndex(TasksResponse.TASK_COLUMN_CATEGORY)));
                notes.add(note);


            } while (cursor.moveToNext());
        }



        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TasksResponse.TASK_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public void deleteNote(TasksResponse note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TasksResponse.TASK_TABLE_NAME, TasksResponse.TASK_COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getTask_id())});
        db.close();
    }

    public class TasksResponse{
        public static final String TASK_TABLE_NAME = "tasks_table";
        public static final String TASK_COLUMN_ID = "task_id";

        public static final String TASK_COLUMN_TITTLE = "task_title";
        public static final String TASK_COLUMN_NAME="task_name";
        public static final String TASK_COLUMN_DATE="task_date";
        public static final String TASK_COLUMN_TIME="task_time";
        public static final String TASK_COLUMN_REPEAT="task_repeat";
        public static final String TASK_COLUMN_CATEGORY="task_category";


        public static final String CREATE_TASKS_TABLE="CREATE TABLE " + TASK_TABLE_NAME
                + "("
                + TASK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +TASK_COLUMN_TITTLE+ " TEXT,"
                +TASK_COLUMN_NAME+ " TEXT,"
                +TASK_COLUMN_DATE+ " TEXT,"
                +TASK_COLUMN_TIME+ " TEXT,"
                +TASK_COLUMN_REPEAT+ " TEXT,"
                +TASK_COLUMN_CATEGORY+ " TEXT"
                + ")";


        private int task_id;
        private String task_title;
        private String task_name;
        private String task_date;
        private String task_time;
        private String task_repeat;
        private String task_category;

        public TasksResponse(int task_id, String task_title, String task_name, String task_date, String task_time, String task_repeat, String task_category) {
            this.task_id = task_id;
            this.task_title = task_title;
            this.task_name = task_name;
            this.task_date = task_date;
            this.task_time = task_time;
            this.task_repeat = task_repeat;
            this.task_category = task_category;
        }

        public TasksResponse() {

        }

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public String getTask_title() {
            return task_title;
        }

        public void setTask_title(String task_title) {
            this.task_title = task_title;
        }

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getTask_date() {
            return task_date;
        }

        public void setTask_date(String task_date) {
            this.task_date = task_date;
        }

        public String getTask_time() {
            return task_time;
        }

        public void setTask_time(String task_time) {
            this.task_time = task_time;
        }

        public String getTask_repeat() {
            return task_repeat;
        }

        public void setTask_repeat(String task_repeat) {
            this.task_repeat = task_repeat;
        }

        public String getTask_category() {
            return task_category;
        }

        public void setTask_category(String task_category) {
            this.task_category = task_category;
        }
    }
}
