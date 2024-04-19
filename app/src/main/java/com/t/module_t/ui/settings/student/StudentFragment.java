package com.t.module_t.ui.settings.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.entity.User;
import com.t.module_t.databinding.FragmentAddStidentsBinding;
import com.t.module_t.ui.settings.ProfileFragment;

import java.util.ArrayList;

public class StudentFragment extends Fragment {

    private String TAG = "Student_set_fragment";
    FragmentAddStidentsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseControl control = new DataBaseControl(getContext());
        binding = FragmentAddStidentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView back = root.findViewById(R.id.back1);
        back.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ProfileFragment()).commit());
        control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), v -> {
            if (v.status) {
                EditText email = root.findViewById(R.id.editTextTextEmailAddress);
                Button button = root.findViewById(R.id.button);
                RecyclerView recyclerView = root.findViewById(R.id.rec_student);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                ArrayList<User> courses = new ArrayList<>();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                StudentsAdapter courseAdapter = new StudentsAdapter(getActivity(), courses, mAuth.getCurrentUser().getEmail());
                control.getStudentsByEmail(mAuth.getCurrentUser().getEmail(), users -> {
                    courses.addAll(users);
                    Log.i(TAG, users.toString());
                    courseAdapter.notifyItemInserted(courses.size() - 1);
                    courseAdapter.notifyDataSetChanged();
                });
                recyclerView.setAdapter(courseAdapter);

                button.setOnClickListener(p -> {
                    String email_ = email.getText().toString();
                    if (!email_.isEmpty())
                        control.getUser(email_, userData -> {
                            // Обрабатываем данные пользователя здесь
                            if (userData != null) {
                                control.checkUserInStudentsByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData, flag -> {
                                    if (flag) {
                                        control.updateByNewStudent(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData);
                                        courses.add(userData);
                                        Log.d(TAG, userData.toString());
                                        courseAdapter.notifyItemInserted(courses.size() - 1);
                                        courseAdapter.notifyDataSetChanged();
                                    }
                                });
                            } else {
                                Toast.makeText(root.getContext(), "Студент не найден", Toast.LENGTH_SHORT).show();
                            }
                        });
                });
            } else {

            }
        });
        return root;
    }
}
