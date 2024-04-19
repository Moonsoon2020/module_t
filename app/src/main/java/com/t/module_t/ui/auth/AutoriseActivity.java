package com.t.module_t.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.t.module_t.ui.MainActivity;
import com.t.module_t.R;
public class AutoriseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.auntef_activity);
        View but0 = findViewById(R.id.buttonAutgLogin);
        View but1 = findViewById(R.id.buttonReg);
        but0.setOnClickListener(v -> {
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
        });
        but1.setOnClickListener(v -> {
            Intent intent = new Intent(this, Registration.class);
            startActivity(intent);
        });
    }
}
