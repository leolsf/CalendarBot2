package hk.hkucs.calendarbot2;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Task;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChatFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private String urlAdress = "https://sentiment-analysis-api.herokuapp.com/sentiment";
    private int[] COLOR_MAP = {
            R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700, R.color.blue_100,
            R.color.blue_300, R.color.blue_500, R.color.blue_700, R.color.green_100, R.color.green_300,
            R.color.green_500, R.color.green_700
    };

    DatabaseHelper mDatabaseHelper;
    private Button button_Send;
    List<Msg> list = new ArrayList<>();
    EditText text;
    final TaskClass task = new TaskClass();
    ImageView imageLeft;

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
        final ItemAdapter msgAdapter = new ItemAdapter(getActivity(), list);
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
                Random random = new Random();
                int i = random.nextInt(17);
                String content = text.getText().toString();
                int indexOf = content.indexOf("+");
                if(!"".equals(content)) {
                    if(content.contains("task+")) {
                        String task_info = content.substring(indexOf).replace("+", "");
                        Msg msg_task = new Msg(content, 0, 1);
                        Msg msg1 = new Msg("OK. Task is "+task_info+", and where?\n(eg: location+CYC Building.)", 0, 0);
                        list.add(msg_task);
                        list.add(msg1);
                        task.setInfo(task_info);
                    }else if(content.contains("location+")) {
                        String location_info = content.substring(indexOf).replace("+", "");
                        Msg msg_location = new Msg(content, 0, 1);
                        Msg msg2= new Msg("Location is "+location_info+", and when?\n(eg: date+2020,5,1)",0, 0);
                        list.add(msg_location);
                        list.add(msg2);
                        task.setLocation(location_info);
                    }else if(content.contains("date+")) {
                        String date_info = content.substring(indexOf).replace("+", "");
                        Msg msg_date = new Msg(content, 0, 1);
                        Msg msg3 = new Msg("Date is "+date_info+", and what time?\n(eg: time+19:00:00)",0, 0);
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
                        Msg msg_time = new Msg(content, 0, 1);
                        Msg msg4 = new Msg("Time is "+time_info,0, 0);
                        Msg msg_done1 = new Msg("Congrats! your new event is recorded.", 0, 0);
                        Msg msg_done2 = new Msg("", Pic[i], 0);
                        list.add(msg_time);
                        list.add(msg4);
                        list.add(msg_done1);
                        list.add(msg_done2);
                        String[] time_list = time_info.split(":");
                        ArrayList<Integer> time_l = new ArrayList<>();
                        for (String s : time_list) {
                            time_l.add(Integer.parseInt(s));
                        }
                        task.setTime(time_l.get(0), time_l.get(1), time_l.get(2));
                        mDatabaseHelper.addTask(task);
                    }else if(content.contains("hi")|content.contains("Hi")|content.contains("hello")|content.contains("Hello")) {
                        Msg msg_other = new Msg(content, 0, 1);
                        Msg msg5 = new Msg("Hi~",0, 0);
                        Msg msg6 = new Msg("", Pic[i], 0);
                        list.add(msg_other);
                        list.add(msg5);
                        list.add(msg6);
                    }else if(content.contains("who")|content.contains("Who")|content.contains("how")|content.contains("How")) {
                        Msg msg_other = new Msg(content, 0, 1);
                        Msg msg5 = new Msg("I can help add your events and plans.", 0, 0);
                        Msg msg6 = new Msg("You could enter a event with the keyword.\n(eg: task+lecture)", 0, 0);
                        list.add(msg_other);
                        list.add(msg5);
                        list.add(msg6);
                    }else if(content.contains("bye")|content.contains("Bye")|content.contains("thank")|content.contains("Thank")) {
                        Msg msg_other = new Msg(content, 0, 1);
                        Msg msg5 = new Msg("Glad to help you~", 0, 0);
                        Msg msg6 = new Msg("", Pic[i], 0);
                        list.add(msg_other);
                        list.add(msg5);
                        list.add(msg6);
                    }else {
                        Msg msg_other = new Msg(content, 0, 1);
                        Msg msg7 = new Msg("Please use keyword+, eg: task+lecture.",0, 0);
                        int[] value = sendPost(content);
//                        int pic_num = -1;
//                        if(value[0]==1){
//                            pic_num = pos_Pic[random.nextInt(pos_Pic.length)];
//                        }
//                        else{
//                            pic_num = neg_Pic[random.nextInt(neg_Pic.length)];
//                        }

                        Msg msg8 = new Msg("", Pic[i], 0);
                        list.add(msg_other);
                        list.add(msg7);
                        list.add(msg8);
                    }

                    msgAdapter.notifyItemInserted(list.size()-1);
                    recyclerView.scrollToPosition(list.size()-1);
                    text.setText("");
                }else {
                    toastMessage("Please enter a event :) ");
                }
            }
        });

    }

    private final int Pic[] = {
            R.drawable.dog2, R.drawable.dog3, R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4,
            R.drawable.cat5, R.drawable.cat6, R.drawable.cat7, R.drawable.cat8, R.drawable.cat9, R.drawable.cat10,
            R.drawable.cat11, R.drawable.cat12, R.drawable.cat13, R.drawable.panda1, R.drawable.panda2
    };

    private final int pos_Pic[] = {R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.cat9, R.drawable.cat12, R.drawable.cat13, R.drawable.dog3};

    private final int neg_Pic[] = {R.drawable.cat5, R.drawable.cat6, R.drawable.cat7, R.drawable.cat8, R.drawable.cat10, R.drawable.cat11, R.drawable.dog2, R.drawable.panda1, R.drawable.panda2};

    public void initData(){
        Random random = new Random();
        int i = random.nextInt(6);
        Msg msg_init1 = new Msg("Hi~, I am your personal assistant.",0, 0);
        Msg msg_init2 = new Msg("I can help record your events and plans.",0, 0);
        Msg msg_init3 = new Msg("Please tell me your new event.\n(eg: task+lecture.)",0, 0);
        Msg msg_init4 = new Msg("", Pic[i],0);
        list.add(msg_init1);
        list.add(msg_init2);
        list.add(msg_init4);
        list.add(msg_init3);
    }

    private int[] sendPost(final String msg) {
        final int[] value = new int[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlAdress);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("text", msg);


                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer r = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        r.append(inputLine);
                    }
                    in.close();

                    String response = r.toString();

                    if(response=="Positive"){
                        value[0] = 1;
                    }
                    else{
                        value[1] = 0;
                    }


                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    Log.i("Response" , response);


                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        return value;

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}