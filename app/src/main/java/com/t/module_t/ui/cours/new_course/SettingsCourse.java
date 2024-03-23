package com.t.module_t.ui.cours.new_course;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.t.module_t.R;

public class SettingsCourse extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_course);
        EditText editText = findViewById(R.id.editTextTextEmailAddress2);
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(v ->{

        });
    }
}
