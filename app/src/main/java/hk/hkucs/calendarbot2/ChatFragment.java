package hk.hkucs.calendarbot2;

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

import java.util.ArrayList;
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

    List<Msg> list  =new ArrayList<>();
    EditText text ;

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
                String newEntry = text.getText().toString();
                if(text.length() != 0) {
                    AddData(newEntry);
//                    text.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

                Random random = new Random();
                // 这里还是利用随机数来生成消息的类型
                int count = random.nextInt(10);
                Msg msg = new Msg(text.getText()+"count: "+count, count%2);
                list.add(msg);
                // 表示在消息的末尾插入内容
                msgAdapter.notifyItemInserted(list.size()-1);
                // 让 RecyclerView 自动滚动到最底部
                recyclerView.scrollToPosition(list.size()-1);
                // 清空内容
                text.setText("");
            }
        });

    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);
        if (insertData) {
            toastMessage("Data inserted successfully :)");
        } else {
            toastMessage("Something went wrong :(");
        }
    }

    public void initData(){
        Random random = new Random();
        for (int i=0;i<40;i++){
            int count = random.nextInt(10);
            Msg msg = new Msg("message"+i+"count: "+count,count%2);
            list.add(msg);
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}