package com.t.module_t.database;

import java.util.Date;
import java.util.HashMap;

public class Message implements Comparable<Message>{
    public String text;
    public String by;
    public Date date;
    public Message(String text, String email_by){
        this.date = new Date();
        this.text = text;
        by = email_by;
    }
    public Message(HashMap<String, Object> map) {
        this.text = map.get("text").toString();
        this.by = map.get("by").toString();
        HashMap<String, Object> dateData = (HashMap<String, Object>) map.get("date");
        long timeInMillis = Long.parseLong(dateData.get("time").toString());
        this.date = new Date(timeInMillis);
    }
    @Override
    public int compareTo(Message other) {
        return this.date.compareTo(other.date);
    }
}
