package com.t.module_t;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t.module_t.database.DataBaseControl;

public class Registration extends AppCompatActivity {
    private final String TAG = "Registration";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        Button button = findViewById(R.id.buttonReg);
        Log.i(TAG, "Загрузка активити");


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.editTextRegEmail);
            EditText editTextPass = findViewById(R.id.editTextRegPassword);
            EditText editTextName = findViewById(R.id.editTextRegName);
            Switch aSwitch = findViewById(R.id.switchRegStatus);
            Log.i(TAG, "Создание пользователя");
            String email, password;
            email = editText.getText().toString();
            password = editTextPass.getText().toString();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(Registration.this, "email не могжет быть пустым",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 7){
                Toast.makeText(Registration.this, "пароль не могжет быть меньше 7 знаков",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                DataBaseControl control = new DataBaseControl();
                                String name = editTextName.getText().toString();
                                boolean status = aSwitch.isChecked();
                                control.addUser(email, password, name, status, user.getUid().toString());
                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                if (task.getException()..toString().equals("FirebaseAuthInvalidCredentialsException")){
//                                    Toast.makeText(Registration.this, "Имейл включает в себя @",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                                else {
                                    Toast.makeText(Registration.this, "Что-то не так, проверьте соединение с интернетом",
                                            Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }
                    });
            // editText.getText(); editTextPass.getText();
        });
    }
}
