package com.t.module_t.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessViewModel extends ViewModel {
    private final String TAG = "HomeViewModel";

    private final MutableLiveData<String> mText;

    public MessViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}