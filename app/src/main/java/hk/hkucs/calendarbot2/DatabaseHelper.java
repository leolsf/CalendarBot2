package hk.hkucs.calendarbot2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

/***
 * Created by Leo on 3/29/2020
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "task_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "task";

//    private static final String TABLE_NAME2 = "task_table2";
//    private static final String YEAR = "year";
//    private static final String MONTH = "year";
//    private static final String DAY = "year";
//    private static final String HOUR = "year";
//    private static final String MINUTE = "year";
//    private static final String SECOND = "year";
//    private static final String LOCATION = "year";
//    private static final String INFO = "year";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        // if date as inserted incorrectly, it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the task passed in
     * @param task
     * @return
     */
    public Cursor getItemID(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME
                + " WHERE " + COL2 + " = '" + task + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the task name field
     * @param newTask
     * @param id
     * @param oldTask
     */
    public void updateTask(String newTask, int id, String oldTask) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '"
                + newTask + "' WHERE " + COL1 + " = '" + id + "'"
                + " AND " + COL2 + " = '" + oldTask + "'";
        Log.d(TAG, "updateTask: query: " + query);
        Log.d(TAG, "updateTask: Setting task to " + newTask);
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param task
     */
    public void deleteName(int id, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" + " AND "
                + COL2 + " = '" + task + "'";
        Log.d(TAG, "deleteTask: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + task + " from database.");
        db.execSQL(query);
    }
}
