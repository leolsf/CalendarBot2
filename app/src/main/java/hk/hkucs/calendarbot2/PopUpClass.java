package hk.hkucs.calendarbot2;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import static hk.hkucs.calendarbot2.R.layout.pop_up_layout;

public class PopUpClass {

    public void showPopupWindow(final View view, Context c, String[] taskArray){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(pop_up_layout,null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
        ArrayAdapter adapter = new ArrayAdapter<String>(c, R.layout.activity_listview, taskArray);
        ListView listView = (ListView) popupView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
//        TextView tv = popupView.findViewById(R.id.hello);
//        tv.setText("Display a list of tasks");
    }
}
