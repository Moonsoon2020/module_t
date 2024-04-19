package com.t.module_t.database.callbacks;



import com.t.module_t.database.entity.User;

import java.util.ArrayList;

public interface UserArrayCallback {

    void onUserArrayFetch(ArrayList<User> strings);

}