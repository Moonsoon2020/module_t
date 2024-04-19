package com.t.module_t.ui.cours.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.entity.User;
import com.t.module_t.ui.cours.setting.StudentsAdapter;

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
        DataBaseControl control = new DataBaseControl(this);
        control.getStudentsByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData -> {
            array.addAll(userData);
            adapter.notifyDataSetChanged();
        });

    }
}
