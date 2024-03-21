package com.t.module_t.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.t.module_t.Authentication;
import com.t.module_t.R;
import com.t.module_t.database.BoolCallback;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.databinding.FragmentSettingBinding;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {
    private final String TAG = "SettingsFragment";

    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DataBaseControl control = new DataBaseControl();
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setCustomView(R.layout.settings_toolbar);
        bar.setDisplayShowCustomEnabled(true);
        control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), v->{
            if (((User)v).status){
                EditText email = root.findViewById(R.id.editTextTextEmailAddress);
                Button button = root.findViewById(R.id.button);
                RecyclerView recyclerView = root.findViewById(R.id.rec_student);email.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
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
            } else{

            }
        });
        Button button_out = bar.getCustomView().findViewById(R.id.button_settings_toolbar_out);
        button_out.setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            Intent mIntent = new Intent(root.getContext(), Authentication.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent);
            if (getActivity() != null) {
                getActivity().finish();
            }
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