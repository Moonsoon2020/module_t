package com.t.module_t.database.callbacks;

import com.t.module_t.database.entity.Notification;

import java.util.ArrayList;

public interface NotificationArrayCallback {
    void onNotificationArrayFetch(ArrayList<Notification> notifications);
}
