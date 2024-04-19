package com.t.module_t.database.callbacks;

import com.t.module_t.database.entity.Notification;

public interface NotificationCallback {
    void onNotificationFetch(Notification notification);
}
