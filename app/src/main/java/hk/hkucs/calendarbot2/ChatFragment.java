package hk.hkucs.calendarbot2;

import android.content.Context;
import android.content.Intent;
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

public class ChatFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    private int[] COLOR_MAP = {
            R.color.red_100, R.color.red_300, R.color.red_500, R.color.red_700, R.color.blue_100,
            R.color.blue_300, R.color.blue_500, R.color.blue_700, R.color.green_100, R.color.green_300,
            R.color.green_500, R.color.green_700
    };

    DatabaseHelper mDatabaseHelper;
    private Button button_Add;
    private EditText editText_Input;

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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false);

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        editText_Input = (EditText) view.findViewById(R.id.editText_Input);
        button_Add = (Button) view.findViewById(R.id.button_Add);
        mDatabaseHelper = new DatabaseHelper(getActivity());

        return view;
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
        TextView textViewCounter = view.findViewById(R.id.chat_counter);
        textViewCounter.setText("Chat");
//        if(counter == 0){
//            textViewCounter.setText("Calendar");
//        }
//        else{
//            textViewCounter.setText("Chat");
//        }

        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEntry = editText_Input.getText().toString();
                if(editText_Input.length() != 0) {
                    AddData(newEntry);
                    editText_Input.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }
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

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
