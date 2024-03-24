package com.t.module_t.ui.cours.new_course;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.Notification;
import com.t.module_t.database.User;
import com.t.module_t.ui.notifications.NotificationAdapter;

import java.util.ArrayList;

public class SettingsCourse extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_course);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ArrayList<User> array = new ArrayList<>();
        Intent intent = getIntent();
        StudentsAdapter adapter = new StudentsAdapter(this, array, intent.getStringExtra("id_course"));
        recyclerView.setAdapter(adapter);
        DataBaseControl control = new DataBaseControl();
        control.getStudentsByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData -> {
            array.addAll(userData);
            adapter.notifyDataSetChanged();
        });

    }
}
