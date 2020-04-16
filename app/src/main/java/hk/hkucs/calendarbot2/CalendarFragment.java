package hk.hkucs.calendarbot2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CalendarFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    static private Context c;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
    @Override public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
        MaterialCalendarView calendarView = view.findViewById(R.id.simpleCalendarView);

        CalendarDay date1 = CalendarDay.from(2020,3,1);
        CalendarDay date2 = CalendarDay.from(2020,3,2);
        CalendarDay date3 = CalendarDay.from(2020,3,3);
        CalendarDay date4 = CalendarDay.from(2020,3,4);




        ArrayList<CalendarDay> dates = new ArrayList<CalendarDay>(Arrays.asList(date1, date2, date3, date4));

        EventDecorator eventDecorator = new EventDecorator(COLOR_MAP[0],dates);
        calendarView.addDecorator(eventDecorator);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                PopUpClass popUpClass = new PopUpClass();
                String[] taskArray = {"Lecture","Appointment","Presentation","Seminar"};
                popUpClass.showPopupWindow(widget, c, taskArray);

            }
        });


//        TextView textViewCounter = view.findViewById(R.id.calendar_counter);
//        textViewCounter.setText("Calendar");
//        if(counter == 0){
//            textViewCounter.setText("Calendar");
//        }
//        else{
//            textViewCounter.setText("Chat");
//        }


    }
}