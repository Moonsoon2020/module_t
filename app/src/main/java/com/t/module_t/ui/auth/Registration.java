package com.t.module_t.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.t.module_t.ui.MainActivity;
import com.t.module_t.R;
import com.t.module_t.database.control.DataBaseControl;

public class Registration extends AppCompatActivity {
    private final String TAG = "Registration";
    private String email, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.registration);
        View button = findViewById(R.id.buttonReg);
        Log.i(TAG, "Загрузка активити");

        TextView textView = findViewById(R.id.textView10);
        textView.setOnClickListener(v -> finish());
        button.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.editTextRegEmail);
            EditText editTextPass = findViewById(R.id.editTextRegPassword);
            EditText editTextName = findViewById(R.id.editTextRegName);
            Switch aSwitch = findViewById(R.id.switchRegStatus);
            Log.i(TAG, "Создание пользователя");
            email = editText.getText().toString();
            password = editTextPass.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Registration.this, "email не могжет быть пустым",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 7) {
                Toast.makeText(Registration.this, "пароль не могжет быть меньше 7 знаков",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            DataBaseControl control = new DataBaseControl(this);
                            String name = editTextName.getText().toString();
                            boolean status = aSwitch.isChecked();
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(task1 -> {
                                        if (!task1.isSuccessful()) {
                                            Log.w(TAG, "Fetching FCM registration token failed", task1.getException());
                                            return;
                                        }

                                        String token = task1.getResult();
                                        control.addUser(email, name, status, token);
                                        Log.d(TAG, token);
                                        Intent intent = new Intent(Registration.this, MainActivity.class);

                                        startActivity(intent);
                                    });
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if (task.getException().getClass() == FirebaseNetworkException.class) {
                                Toast.makeText(Registration.this, "Нет сети. Проверьте подключение к интернету",
                                        Toast.LENGTH_SHORT).show();
                            } else if (task.getException().getClass() == FirebaseAuthUserCollisionException.class) {
                                Toast.makeText(Registration.this, "Проверьте правильность введённого email",
                                        Toast.LENGTH_SHORT).show();
                            } else
                            Toast.makeText(Registration.this, "Что-то пошло не так, попробуйте, позже.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
