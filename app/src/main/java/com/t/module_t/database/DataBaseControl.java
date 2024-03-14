package com.t.module_t.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBaseControl {
    private String TAG = "DataBaseControl";
    private DatabaseReference mDatabase;
    public DataBaseControl(){
        Log.i(TAG, "constructor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void addUser(String email,String password, String name, Boolean status, String id){
        User user = new User(name, email, status, password);
        mDatabase.child("users").child(id).setValue(user);
    }

    public void getUser(String id){
        mDatabase.child("users").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

    }
}
