package hk.hkucs.calendarbot2;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class CalendarFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    static private Context c;
    private DatabaseHelper databaseHelper;
    private MaterialCalendarView calendarView;
    private EventDecorator currentDecorator;

    private int[] COLOR_MAP = {
            R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700, R.color.blue_100,
            R.color.blue_300, R.color.blue_500, R.color.blue_700, R.color.green_100, R.color.green_300,
            R.color.green_500, R.color.green_700
    };
    public CalendarFragment() {
        // Required empty public constructor
    }
    public static CalendarFragment newInstance(Integer counter, Context fragmentActivity) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        c = fragmentActivity;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        if(calendarView!=null){
            calendarView.removeDecorators();
            ArrayList<CalendarDay> dates = databaseHelper.getAllDates();
            currentDecorator = new EventDecorator(COLOR_MAP[0],dates);
            TodayDecorator todayDecorator = new TodayDecorator(COLOR_MAP[1]);
            calendarView.addDecorator(currentDecorator);
            calendarView.addDecorator(todayDecorator);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
    @Override public void onViewCreated(@NonNull final  View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
        calendarView = view.findViewById(R.id.simpleCalendarView);

        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelper.addSomeTestRecords();

        ArrayList<CalendarDay> dates = databaseHelper.getAllDates();
        EventDecorator eventDecorator = new EventDecorator(COLOR_MAP[0],dates);
        TodayDecorator todayDecorator = new TodayDecorator(COLOR_MAP[1]);
        calendarView.addDecorator(eventDecorator);
        calendarView.addDecorator(todayDecorator);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                final ArrayList<String> taskArray = new ArrayList<>();
                final ArrayList<TaskClass> task_list = databaseHelper.getTasksByDate(date);
                for(int i = 0; i<task_list.size(); i++){
                    TaskClass taskClass = task_list.get(i);
                    String t = "";
                    if(taskClass.getTime()[0]<10){
                        t += "0"+taskClass.getTime()[0];
                    }else{
                        t += taskClass.getTime()[0];
                    }
                    t += ":";
                    if(taskClass.getTime()[1]<10){
                        t += "0"+taskClass.getTime()[1];
                    }else{
                        t += taskClass.getTime()[1];
                    }
                    t += ":";
                    if(taskClass.getTime()[2]<10){
                        t += "0"+taskClass.getTime()[2];
                    }else{
                        t += taskClass.getTime()[2];
                    }
                    String text = t+"   "+taskClass.getInfo();
                    taskArray.add(text);
                }
                if(taskArray.size()!=0){
                    LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.pop_up_layout,null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    final PopupWindow popupWindow = new PopupWindow(popupView,width,height,true);
                    popupWindow.setOutsideTouchable(true);

                    popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                    ArrayAdapter adapter = new ArrayAdapter<String>(c, R.layout.activity_listview, taskArray);
                    final ListView listView = (ListView) popupView.findViewById(R.id.listView);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(c, EditDataActivity.class);
                            intent.putExtra("id",id);
                            TaskClass task = task_list.get((int) id);
                            intent.putExtra("task_info", task.getInfo());
                            intent.putExtra("year", task.getDate()[0]);

                            intent.putExtra("month", task.getDate()[1]);
                            intent.putExtra("day", task.getDate()[2]);
                            intent.putExtra("hour", task.getTime()[0]);
                            intent.putExtra("minute", task.getTime()[1]);
                            intent.putExtra("second", task.getTime()[2]);
                            intent.putExtra("location", task.getLocation());
                            startActivity(intent);
                            popupWindow.dismiss();
                        }
                    });
                }


            }
        });



    }
}