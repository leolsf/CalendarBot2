package hk.hkucs.calendarbot2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

/***
 * Created by Leo on 3/20/2020
 */

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button button_Save, button_Delete;
    private EditText editText_Task, editText_location, editText_date, editText_time;

    DatabaseHelper mDatabaseHelper;

    private String selectedTask;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit);
        button_Save = (Button)findViewById(R.id.button_Save);
        button_Delete = (Button)findViewById(R.id.button_Delete);
        editText_Task = (EditText)findViewById(R.id.editText_Task);
        editText_location = (EditText)findViewById(R.id.editText_location);
        editText_date = (EditText)findViewById(R.id.editText_date);
        editText_time = (EditText)findViewById(R.id.editText_time);
        mDatabaseHelper = new DatabaseHelper(this);

        // get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        // now get the task name we passed as an extra
        final TaskClass taskClass = new TaskClass();
        taskClass.setInfo(receivedIntent.getStringExtra("task_info"));
        taskClass.setLocation(receivedIntent.getStringExtra("location"));
        taskClass.setDate(receivedIntent.getIntExtra("year",1970), receivedIntent.getIntExtra("month",1), receivedIntent.getIntExtra("day",1));
        taskClass.setTime(receivedIntent.getIntExtra("hour",0), receivedIntent.getIntExtra("minute",0), receivedIntent.getIntExtra("second",0));
        // set the text to show the current selected task name
        editText_Task.setText(taskClass.getInfo());
        editText_location.setText(taskClass.getLocation());
        editText_date.setText(taskClass.getDate()[0]+","+taskClass.getDate()[1]+","+taskClass.getDate()[2]);
        editText_time.setText(taskClass.getTime()[0]+":"+taskClass.getTime()[1]+":"+taskClass.getTime()[2]);
//
        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = editText_Task.getText().toString();
                String date = editText_date.getText().toString();
                String time = editText_time.getText().toString();
                String location = editText_location.getText().toString();
                if(!item.equals("") && !date.equals("") && !time.equals("") && !location.equals("")) {
                    mDatabaseHelper.deleteTask(taskClass);
                    taskClass.setInfo(item);
                    taskClass.setLocation(location);
                    String[] date_list = date.split(",");
                    String[] time_list = time.split(":");
                    ArrayList<Integer> date_l = new ArrayList<>();
                    for (String s : date_list) {
                        date_l.add(Integer.parseInt(s));
                    }
                    taskClass.setDate(date_l.get(0), date_l.get(1), date_l.get(2));
                    ArrayList<Integer> time_l = new ArrayList<>();
                    for (String s : time_list) {
                        time_l.add(Integer.parseInt(s));
                    }
                    taskClass.setTime(time_l.get(0), time_l.get(1), time_l.get(2));
                    mDatabaseHelper.addTask(taskClass);
                    ((Activity)EditDataActivity.this).finish();
                } else {
                    toastMessage("You must enter a task :) ");
                }

            }
        });
//
        button_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteTask(taskClass);
                editText_Task.setText("");
                toastMessage("removed from database");
                ((Activity)EditDataActivity.this).finish();

            }
        });

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
