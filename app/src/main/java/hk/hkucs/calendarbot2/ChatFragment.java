package hk.hkucs.calendarbot2;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChatFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private int[] COLOR_MAP = {
            R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700, R.color.blue_100,
            R.color.blue_300, R.color.blue_500, R.color.blue_700, R.color.green_100, R.color.green_300,
            R.color.green_500, R.color.green_700
    };

    DatabaseHelper mDatabaseHelper;
    private Button button_Send;
    private EditText editText_Input;

    List<Msg> list = new ArrayList<>();
    EditText text;
    final TaskClass task = new TaskClass();

    public ChatFragment() {
        // Required empty public constructor
    }
    public static ChatFragment newInstance(Integer counter) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
        initData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment`
//        return inflater.inflate(R.layout.fragment_chat, container, false);

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        text = (EditText) view.findViewById(R.id.et_info);
        button_Send = (Button) view.findViewById(R.id.bt_send);
        mDatabaseHelper = new DatabaseHelper(getActivity());

        return view;
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
        final RecyclerView recyclerView = view.findViewById(R.id.rlv);
        final ItemAdapter msgAdapter = new ItemAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgAdapter);
//        if(counter == 0){
//            textViewCounter.setText("Calendar");
//        }
//        else{
//            textViewCounter.setText("Chat");
//        }

        button_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String newEntry = text.getText().toString();
//                if(text.length() != 0) {
//                    AddData(newEntry);
////                    text.setText("");
//                } else {
//                    toastMessage("You must put something in the text field!");
//                }

                String content = text.getText().toString();
                int indexOf = content.indexOf("+");
                if(!"".equals(content)) {
                    if(content.contains("task+")) {
                        String task_info = content.substring(indexOf).replace("+", "");
                        Msg msg1 = new Msg("Task is "+task_info+", and where?\n(eg: location+CYC Building.)",0);
                        Msg msg_task = new Msg(content,1);
                        list.add(msg_task);
                        list.add(msg1);
                        task.setInfo(task_info);
                    }else if(content.contains("location+")) {
                        String location_info = content.substring(indexOf).replace("+", "");
                        Msg msg2= new Msg("Location is "+location_info+", and when?\n(eg: date+2020,4,1)",0);
                        Msg msg_location = new Msg(content,1);
                        list.add(msg_location);
                        list.add(msg2);
                        task.setLocation(location_info);
                    }else if(content.contains("date+")) {
                        String date_info = content.substring(indexOf).replace("+", "");
                        Msg msg3 = new Msg("Date is "+date_info+", and what time?\n(eg: time+19:00:00)",0);
                        Msg msg_date = new Msg(content,1);
                        list.add(msg_date);
                        list.add(msg3);
                        String[] date_list = date_info.split(",");
                        ArrayList<Integer> date_l = new ArrayList<>();
                        for (String s : date_list) {
                            date_l.add(Integer.parseInt(s));
                        }
                        task.setDate(date_l.get(0), date_l.get(1), date_l.get(2));
                    }else if(content.contains("time+")) {
                        String time_info = content.substring(indexOf).replace("+", "");
                        Msg msg4 = new Msg("Time is "+time_info,0);
                        Msg msg_done = new Msg("Congrats, your new task is recorded.", 0);
                        Msg msg_time = new Msg(content,1);
                        list.add(msg_time);
                        list.add(msg4);
                        list.add(msg_done);
                        String[] time_list = time_info.split(":");
                        ArrayList<Integer> time_l = new ArrayList<>();
                        for (String s : time_list) {
                            time_l.add(Integer.parseInt(s));
                        }
                        task.setTime(time_l.get(0), time_l.get(1), time_l.get(2));
                        mDatabaseHelper.addTask(task);
                    }else {
                        Msg msg5 = new Msg("Please input with keyword+, eg: task+lecture.",0);
                        Msg msg_other = new Msg(content,1);
                        list.add(msg_other);
                        list.add(msg5);
                    }

                    msgAdapter.notifyItemInserted(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                    text.setText("");
                }else {
                    toastMessage("You must enter a task :) ");
                }
            }
        });

    }

    public void AddData(String i) {
//        task.setDate(d);
//        task.setTime(t);
//        task.setLocation(l);
//        task.setInfo(i);
//        mDatabaseHelper.deleteTask(task);

        boolean insertData = mDatabaseHelper.addTask(task);
        if (insertData) {
            toastMessage("Data inserted successfully :)");
        } else {
            toastMessage("Something went wrong :(");
        }
    }

    public void initData(){
        Msg msg_init = new Msg("Hi, I am your personal assistant.",0);
        Msg msg_ask = new Msg("Please input your task, location, date and time with the keyword.",0);
        Msg msg_example = new Msg("What is your new task?\n(eg: task+lecture.)",0);
        list.add(msg_init);
        list.add(msg_ask);
        list.add(msg_example);
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}