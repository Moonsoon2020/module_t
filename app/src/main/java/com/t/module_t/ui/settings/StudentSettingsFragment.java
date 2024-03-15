package com.t.module_t.ui.settings;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.UserCallback;
import com.t.module_t.databinding.FragmentNotificationsBinding;
import com.t.module_t.databinding.FragmentSettingStudentBinding;
import com.t.module_t.ui.notifications.NotificationsViewModel;

import java.util.HashMap;

public class StudentSettingsFragment  extends Fragment {
    private final String TAG = "StudentSettingsFragment";

    private FragmentSettingStudentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StudentSettingsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(StudentSettingsViewModel.class);

        binding = FragmentSettingStudentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText email = root.findViewById(R.id.editTextTextEmailAddress);
        Button button = root.findViewById(R.id.button);
        button.setOnClickListener(v ->{
            DataBaseControl control = new DataBaseControl();
            control.getUser(email.getText().toString(), new UserCallback() {
                @Override
                public void onUserFetch(Object userData) {
                    // Обрабатываем данные пользователя здесь
                    if (userData != null) {
                        Log.i(TAG, userData.toString());
                    } else {
                        //Toast.makeText(root.getContext(), "Внутренняя ошибка", Toast.LENGTH_SHORT);
                        // Обрабатываем ошибку или отсутствие данных
                    }
                }
            });
        });
//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
