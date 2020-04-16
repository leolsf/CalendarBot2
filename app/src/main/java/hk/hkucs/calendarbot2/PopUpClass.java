package hk.hkucs.calendarbot2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import static hk.hkucs.calendarbot2.R.layout.pop_up_layout;

public class PopUpClass extends Fragment {

    private static final String TAG = "PopUpClass";


    public void showPopupWindow(final View view, Context c, final ArrayList<String> taskArray){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(pop_up_layout,null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
//        popupWindow.setOutsideTouchable(true);
//        popupView.setBackground(new ColorDrawable(0x00000000));
        ArrayAdapter adapter = new ArrayAdapter<String>(c, R.layout.activity_listview, taskArray);
        final ListView listView = (ListView) popupView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
//        TextView tv = popupView.findViewById(R.id.hello);
//        tv.setText("Display a list of tasks");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d(TAG, "onItemClick: name: " + id);
//                toastMessage("Click successfully");
//                Intent intent = new Intent(getActivity(), ListDataActivity.class);
//                startActivity(intent);
//                popupWindow.dismiss();
            }
        });
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
