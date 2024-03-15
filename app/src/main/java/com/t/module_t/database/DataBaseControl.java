package com.t.module_t.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataBaseControl {
    private String TAG = "DataBaseControl";
    private DatabaseReference mDatabase;
    public DataBaseControl(){
        Log.i(TAG, "constructor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    private String translate(String email){
        return email.replace(".", "~");
    }

    public void addUser(String email,String password, String name, Boolean status, String id){
        email = translate(email);
        User user = new User(name, email, status, password);
        mDatabase.child("users").child(email).setValue(user);
    }

    public void getUser(String email, final UserCallback callback){
        email = translate(email);
        mDatabase.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Ошибка при получении данных", task.getException());
                    callback.onUserFetch(null); // Уведомляем обратный вызов о получении нулевого значения
                }
                else {
                    Object userData = task.getResult().getValue();
                    Log.d("firebase", String.valueOf(userData));
                    User user = new User((HashMap<String, Object>) userData);
                    callback.onUserFetch(user); // Уведомляем обратный вызов о получении данных пользователя
                }
            }
        });
    }

    // Определяем интерфейс обратного вызова


}
