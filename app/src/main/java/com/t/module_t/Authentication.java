package com.t.module_t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Authentication extends AppCompatActivity {
    private final String TAG = "auntefication";
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
        setContentView(R.layout.auntefication);
        Button button = findViewById(R.id.buttonAutgLogin);
        Log.i(TAG, "Загрузка активити аунтефикации");
        Button button1 = findViewById(R.id.buttonAuthReg);
        button.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.editTextAuthEmail);
            EditText editTextPass = findViewById(R.id.editTextAuthPassword);
            Log.i(TAG, "Ввод информации");
            String email, password;
            email = editText.getText().toString();
            password = editTextPass.getText().toString();
            if (email.isEmpty() || password.isEmpty()){
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Authentication.this, MainActivity.class);
                            startActivity(intent);
//                                updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Authentication.this, "Что-то пошло не так, проверьте подключение к интернету, или запросите изменение пароля у разработчика.",
                                    Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                        }
                    }
                });
        });
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(this, Registration.class);
            Log.i(TAG, "Переход на активити регистрации");
            startActivity(intent);
        });
    }
}
