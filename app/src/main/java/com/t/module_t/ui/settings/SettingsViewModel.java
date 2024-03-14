package com.t.module_t.ui.settings;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private final String TAG = "SettingsViewModel";

    private final MutableLiveData<String> mText;

    public SettingsViewModel() {
        Log.i(TAG, "constructor");
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}