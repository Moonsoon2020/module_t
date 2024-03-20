package com.t.module_t.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBaseControl {
    private String TAG = "DataBaseControl";
    private final DatabaseReference mDatabase;

    public DataBaseControl() {
        Log.i(TAG, "constructor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    private String translate(String email) {
        return email.replace(".", "~");
    }

    public void addUser(String email, String name, Boolean status, String id) {
        email = translate(email);
        User user = new User(name, email, status);
        user.notifications.add(new Notification("Привет, это уведомление о регистрации, рады, что ты с нами."));
        mDatabase.child("users").child(email).setValue(user);
    }

    public void getUser(String email, final UserCallback callback) {
        email = translate(email);
        mDatabase.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() == null) {
                    Log.e("firebase", "Ошибка при получении данных", task.getException());
                    callback.onUserFetch(null); // Уведомляем обратный вызов о получении нулевого значения
                } else {
                    Object userData = task.getResult().getValue();
                    Log.d("firebase", String.valueOf(userData));
                    User user = new User((HashMap<String, Object>) userData);
                    callback.onUserFetch(user); // Уведомляем обратный вызов о получении данных пользователя
                }
            }
        });
    }

    public void getStudentsByEmail(String email, final UserArrayCallback callback) {
        getUser(email, v -> {
            if (v != null)
                callback.onUserArrayFetch(v.getStudents());
            else
                callback.onUserArrayFetch(new ArrayList<User>());
        });
    }

    public void updateTeacherByNewStudent(String email, User newuser) {
        Log.d(TAG, email);
        email = translate(email);
        ArrayList<User> users = new ArrayList<>();
        getStudentsByEmail(email, users::addAll);
        users.add(newuser);
        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Пользователь " + email + "добавил вас к своим ученикам"));
        getNotificationByEmail(newuser.email, v->{
            notifications.addAll(v);
            mDatabase.child("users").child(translate(newuser.email)).child("notifications").setValue(notifications);

        });
        mDatabase.child("users").child(email).child("students").setValue(users);
    }

    public void getNotificationByEmail(String email,  final NotificationArrayCallback callback) {
        email = translate(email);
        getUser(email, v -> {
            if (v != null){
                callback.onNotificationArrayFetch(v.notifications);
            } else
                callback.onNotificationArrayFetch(new ArrayList<>());
        });
    }

    public void checkUserInStudentsByEmail(String email, User userData, BoolCallback callback) {
        getUser(email, v -> {
            if (v != null) {
                for (User user : v.students) {
                    if (Objects.equals(user.getEmail(), userData.getEmail())) {
                        callback.onBoolFetch(false);
                        return;
                    }
                }
                callback.onBoolFetch(true);
            } else
                callback.onBoolFetch(false);
        });
    }

    public void deleteUserOfStudents(String teacher, String email) {
        teacher = translate(teacher);
        ArrayList<User> users = new ArrayList<>();
        getStudentsByEmail(teacher, users::addAll);
        users.removeIf(user -> Objects.equals(user.getEmail(), email));
        mDatabase.child("users").child(teacher).child("students").setValue(users);
        String finalTeacher = teacher;
        getUser(email, v ->{
            ArrayList<Notification> arr = v.notifications;
            arr.add(new Notification("Преподаватель " + finalTeacher + "удалил вас, как своего студента."));
            mDatabase.child("users").child(translate(email)).child("notifications").setValue(arr);

        });
    }

    public void deleteNotifyOfUser(List<Notification> data, String user) {
        mDatabase.child("users").child(translate(user)).
                child("notifications").setValue(data);

    }

    // Определяем интерфейс обратного вызова
}
