package hk.hkucs.calendarbot2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static android.content.ContentValues.TAG;

/***
 * Created by Leo on 3/29/2020
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "task_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "task";

    private static final String TABLE_NAME2 = "task_table2";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECOND = "second";
    private static final String LOCATION = "location";
    private static final String INFO = "info";


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + YEAR + " INT,"
                + MONTH + " INT,"
                + DAY + " INT,"
                + HOUR + " INT,"
                + MINUTE + " INT,"
                + SECOND + " INT,"
                + LOCATION + " TEXT,"
                + INFO + " TEXT"
        +")";
        db.execSQL(createTable);
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
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

    public boolean addTask(TaskClass taskClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(YEAR, taskClass.getDate()[0]);
        contentValues.put(MONTH, taskClass.getDate()[1]);
        contentValues.put(DAY, taskClass.getDate()[2]);

        contentValues.put(HOUR, taskClass.getTime()[0]);
        contentValues.put(MINUTE, taskClass.getTime()[1]);
        contentValues.put(SECOND, taskClass.getTime()[2]);

        contentValues.put(LOCATION, taskClass.getLocation());
        contentValues.put(INFO, taskClass.getInfo());

        //Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME2;

        long result = db.insert(TABLE_NAME2, null, contentValues);

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

    public Cursor getCursorByDate(CalendarDay date){
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, LOCATION, INFO};

        Cursor c = db.query(
                TABLE_NAME2, columns,
                YEAR  + "=" + year  + " AND " +
                        MONTH + "=" + month + " AND " +
                        DAY   + "=" + day,
                null, null, null,
                HOUR + "," + MINUTE);
        return c;

    }

    public ArrayList<TaskClass> getTasksByDate(CalendarDay date){
        Cursor cursor = getCursorByDate(date);
        ArrayList<TaskClass> task_list = new ArrayList<>();
        while(cursor.moveToNext()){
            int index;
//            index = cursor.getColumnIndexOrThrow("year");
//            int year = cursor.getInt(index);
//
//            index = cursor.getColumnIndexOrThrow("month");
//            int month = cursor.getInt(index);
//
//            index = cursor.getColumnIndexOrThrow("day");
//            int day = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow("hour");
            int hour = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow("minute");
            int minute = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow("second");
            int second = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow("location");
            String location = cursor.getString(index);

            index = cursor.getColumnIndexOrThrow("info");
            String info = cursor.getString(index);

            TaskClass taskClass = new TaskClass();
            taskClass.setDate(date);
            taskClass.setTime(hour,minute,second);
            taskClass.setLocation(location);
            taskClass.setInfo(info);
            task_list.add(taskClass);

        }
        return task_list;

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

    public ArrayList<CalendarDay> getAllDates(){
        ArrayList<CalendarDay> date_list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + YEAR + ", "+ MONTH + ", "+ DAY + " FROM " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext()){
            int index;
            index = cursor.getColumnIndexOrThrow("year");
            int year = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow("month");
            int month = cursor.getInt(index);

            index = cursor.getColumnIndexOrThrow("day");
            int day = cursor.getInt(index);

            CalendarDay date = CalendarDay.from(year,month,day);
            date_list.add(date);
        }
        LinkedHashSet<CalendarDay> hashSet = new LinkedHashSet<>(date_list);

        ArrayList<CalendarDay> date_list_no_duplicate = new ArrayList<>(hashSet);
        return date_list_no_duplicate;
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
