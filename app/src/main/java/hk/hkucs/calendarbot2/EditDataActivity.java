package hk.hkucs.calendarbot2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/***
 * Created by Leo on 3/20/2020
 */

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button button_Save, button_Delete;
    private EditText editText_Task;

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
        mDatabaseHelper = new DatabaseHelper(this);

        // get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        // now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id", -1); // note: -1 is just the default value

        // now get the task name we passed as an extra
        selectedTask = receivedIntent.getStringExtra("task");

        // set the text to show the current selected task name
        editText_Task.setText(selectedTask);

        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = editText_Task.getText().toString();
                if(!item.equals("")) {
                    mDatabaseHelper.updateTask(item, selectedID, selectedTask);
                    Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                    startActivity(intent);
                    finish();
//                    EditDataActivity.this.onBackPressed();
                } else {
                    toastMessage("You must enter a task :) ");
                }

            }
        });

        button_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteName(selectedID, selectedTask);
                editText_Task.setText("");
                toastMessage("removed from database");
                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                startActivity(intent);
                finish();
//                EditDataActivity.this.onBackPressed();
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
