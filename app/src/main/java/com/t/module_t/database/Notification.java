package com.t.module_t.database;

import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class Notification {
    public String text;
    public Date date;

    public Notification(String text){
        date = new Date();
        this.text = text;
    }
    Notification(String text, HashMap<String, Long> data){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, data.get("year").intValue() - 100);
        calendar.set(Calendar.MONTH, data.get("month").intValue());
        calendar.set(Calendar.DATE, data.get("date").intValue());
        calendar.set(Calendar.HOUR_OF_DAY, data.get("hours").intValue());
        calendar.set(Calendar.MINUTE, data.get("minutes").intValue());
        calendar.set(Calendar.SECOND, data.get("seconds").intValue());
        calendar.set(Calendar.DAY_OF_WEEK, data.get("day").intValue());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT")); // Установка временной зоны, например, GMT
        // Получение объекта Date из Calendar
        Date dateObject = calendar.getTime();
        this.date = dateObject;
        this.text = text;
    }

}
