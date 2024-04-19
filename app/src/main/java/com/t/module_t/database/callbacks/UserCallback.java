package com.t.module_t.database.callbacks;

import com.t.module_t.database.entity.User;

public interface UserCallback {
    void onUserFetch(User userData);
}