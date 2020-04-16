package hk.hkucs.calendarbot2;

import android.text.format.Time;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import org.bson.Document;


import java.util.Date;

public class TaskClass {
    private CalendarDay date;
    private Date time;
    private String location;
    private String info;
    public void TaskClass(CalendarDay d, Date t, String l, String i){
        date = d;
        time = t;
        location = l;
        info = i;
    }
    public void setDate(CalendarDay d){
        date = d;
    }
    public void setDate(int year, int month, int day){
        CalendarDay d = CalendarDay.from(year, month, day);
        date = d;
    }
    public void setTime(Date t){
        time = t;
    }
    public void setTime(int hour, int minute, int second){
        Date t = new Date();
        t.setHours(hour);
        t.setMinutes(minute);
        t.setSeconds(second);
        time = t;
    }
    public void setLocation(String l){
        location = l;
    }
    public void setInfo(String i){
        info = i;
    }

    public int[] getDate(){
        int[] date_array = {date.getYear(),  date.getMonth(), date.getDay()};
        return date_array;
    }

    public int[] getTime(){
        int[] time_array = {time.getHours(), time.getMinutes(), time.getSeconds()};
        return time_array;
    }

    public String getLocation(){
        return location;
    }

    public String getInfo(){
        return info;
    }

}
