package com.t.module_t.ui.notifications;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {
    private final String TAG = "NotificationsViewModel";

    private final MutableLiveData<String> mText;

    public NotificationsViewModel() {
        Log.i(TAG, "constructor");
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}