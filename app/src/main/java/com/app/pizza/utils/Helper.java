package com.app.pizza.utils;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Helper {

    public static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    public static DateTimeFormatter dataTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String setDataTime(String fullTime)
    {
        LocalDateTime aLDT = LocalDateTime.parse(fullTime);

        return aLDT.format(dataTime);
    }



    public static String setTime(String fullTime, String stepTime) throws ParseException {

        Date d = df.parse(fullTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        Time newTime = Time.valueOf(stepTime);
        cal.add(Calendar.MINUTE, newTime.getMinutes());
        cal.add(Calendar.HOUR, newTime.getHours());
        cal.add(Calendar.SECOND, newTime.getSeconds());
        fullTime = df.format(cal.getTime());

        return fullTime;
    }

}
