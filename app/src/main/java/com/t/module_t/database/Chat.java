package com.t.module_t.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Chat extends ChatFast{
    public ArrayList<Message> messages = new ArrayList<>();
    public Chat(String em1, String em2){
        super(em1, em2);
    }
    public Chat(HashMap<String, Object> chatData) {
        email_teacher = chatData.get("email_teacher").toString();
        email_student = chatData.get("email_student").toString();
        HashMap<String, HashMap<String, Object>> messagesData = (HashMap<String, HashMap<String, Object>>) chatData.get("messages");
        for (String messageId : messagesData.keySet()) {
            HashMap<String, Object> messageData = messagesData.get(messageId);
            messages.add(new Message(messageData));
        }
        Collections.sort(messages);
    }
}
