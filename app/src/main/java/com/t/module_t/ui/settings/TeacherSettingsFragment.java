package com.t.module_t.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.t.module_t.R;
import com.t.module_t.database.BoolCallback;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.database.UserArrayCallback;
import com.t.module_t.database.UserCallback;
import com.t.module_t.databinding.FragmentSettingStudentBinding;
import com.t.module_t.ui.cours.CourseAdapter;
import com.t.module_t.ui.cours.CourseElement;

import java.util.ArrayList;

public class TeacherSettingsFragment extends Fragment {
    private final String TAG = "TeacherSettingsFragment";

    private FragmentSettingStudentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TeacherSettingsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(TeacherSettingsViewModel.class);
        binding = FragmentSettingStudentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText email = root.findViewById(R.id.editTextTextEmailAddress);
        Button button = root.findViewById(R.id.button);
        RecyclerView recyclerView = root.findViewById(R.id.rec_student); // тут проверь это меняли
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ArrayList<User> courses = new ArrayList<>();
        DataBaseControl control = new DataBaseControl();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        StudentsAdapter courseAdapter = new StudentsAdapter(getActivity(), courses, mAuth.getCurrentUser().getEmail());
        control.getStudentsByEmail(mAuth.getCurrentUser().getEmail(), users -> {
            courses.addAll(users);
            Log.i(TAG, users.toString());
            courseAdapter.notifyItemInserted(courses.size() - 1);
            courseAdapter.notifyDataSetChanged();

        });
        recyclerView.setAdapter(courseAdapter);

        button.setOnClickListener(v -> {
            String email_ = email.getText().toString();
            if (!email_.isEmpty())
                control.getUser(email_, userData -> {
                    // Обрабатываем данные пользователя здесь
                    if (userData != null) {
                        control.checkUserInStudentsByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData, new BoolCallback(){
                            @Override
                            public void onBoolFetch(boolean flag) {
                                if (flag){
                                    control.updateTeacherByNewStudent(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData);
                                    courses.add(userData);
                                    Log.d(TAG, userData.toString());
                                    courseAdapter.notifyItemInserted(courses.size() - 1);
                                    courseAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    } else {
                        //Toast.makeText(root.getContext(), "Внутренняя ошибка", Toast.LENGTH_SHORT);
                        // Обрабатываем ошибку или отсутствие данных
                    }
                });
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
