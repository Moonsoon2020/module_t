package com.t.module_t.ui.cours.new_course;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;

public class NewCourse extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_course);
        Button button = findViewById(R.id.button_create_course);
        EditText editText = findViewById(R.id.editTextText);
        button.setOnClickListener(v -> {
            String name = editText.getText().toString();
            DataBaseControl control = new DataBaseControl();
            control.addCourse(name, FirebaseAuth.getInstance().getCurrentUser().getEmail());
            finish();
        });
    }
}
