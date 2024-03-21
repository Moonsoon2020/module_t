package com.t.module_t.database;

import android.text.format.Time;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class Notification{
    public String text;
    public Date date;

    public Notification(String text){
        date = new Date();
        this.text = text;
    }
    public Notification(String text, HashMap<String, Long> data) {
        int year = data.get("year").intValue();
        int month = data.get("month").intValue(); // Учитываем, что месяцы начинаются с 0
        int day = data.get("date").intValue();
        int hour = data.get("hours").intValue();
        int minute = data.get("minutes").intValue();
        int second = data.get("seconds").intValue();

        this.text = text;
        this.date = new Date(year, month, day, hour, minute, second); // Год начинается с 1900
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Notification that = (Notification) obj;
        return Objects.equals(text, that.text) && Objects.equals(date, that.date);
    }
}
