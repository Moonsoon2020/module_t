package com.t.module_t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.t.module_t.database.DataBaseControl;

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
        setContentView(R.layout.auntefication);
        TextView textView = findViewById(R.id.textView10);
        textView.setOnClickListener(v -> finish());
        View button = findViewById(R.id.buttonAutgLogin);
        Log.i(TAG, "Загрузка активити аунтефикации");
        button.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.editTextAuthEmail);
            EditText editTextPass = findViewById(R.id.editTextAuthPassword);
            Log.i(TAG, "Ввод информации");
            String email, password;
            email = editText.getText().toString();
            password = editTextPass.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Authentication.this, "Все поля должны быть заполнены",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(task1 -> {
                                        if (!task1.isSuccessful()) {
                                            Log.w(TAG, "Fetching FCM registration token failed", task1.getException());
                                            return;
                                        }
                                        new DataBaseControl(this).setToken(email, task1.getResult());
                                    });
                            Intent intent = new Intent(Authentication.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if (task.getException().getClass() == FirebaseNetworkException.class) {
                                Toast.makeText(Authentication.this, "Нет сети. Проверьте подключение к интернету",
                                        Toast.LENGTH_SHORT).show();
                            } else if (task.getException().getClass() == FirebaseAuthUserCollisionException.class) {
                                Toast.makeText(Authentication.this, "Проверьте правильность введённого email",
                                        Toast.LENGTH_SHORT).show();
                            } else if (FirebaseAuthInvalidCredentialsException.class == task.getException().getClass()) {
                                Toast.makeText(Authentication.this, "Логин или пароль не совпадают",
                                        Toast.LENGTH_SHORT).show();
                            } else
                            Toast.makeText(Authentication.this, "Что-то пошло не так, попробуйте, позже.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
