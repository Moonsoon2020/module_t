package com.t.module_t.ui.cours;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourseViewModel extends ViewModel {
    private final String TAG = "CourseViewModel";

    private final MutableLiveData<String> mText;

    public CourseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is course fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}