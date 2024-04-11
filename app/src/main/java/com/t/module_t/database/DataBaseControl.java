package com.t.module_t.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DataBaseControl {
    private String TAG = "DataBaseControl";
    private final DatabaseReference mDatabase;
    private Context context;

    public DataBaseControl() {
        Log.i(TAG, "constructor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        context = null;
    }
    public DataBaseControl(Context context) {
        Log.i(TAG, "constructor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    @NonNull
    private String translate(String email) {
        return email.replace(".", "~");
    }

    public void addUser(String email, String name, Boolean status, String token) {
        email = translate(email);
        User user = new User(name, email, status, token);
        mDatabase.child("users").child(email).setValue(user);
        addNotificationForUser("Привет, поздравляю с регистрацией", email , "Уведомления");
    }

    public void getUser(String email, final UserCallback callback) {
        email = translate(email);
        mDatabase.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() == null) {
                    Log.e("firebase", "Ошибка при получении данных" + task.getException());
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

    public void getStudentsEmailByEmail(String email, final UserEmailArrayCallback callback) {
        getUser(email, v -> {
            if (v != null)
                callback.onUserEmailArrayFetch(v.getStudents());
            else
                callback.onUserEmailArrayFetch(new ArrayList<String>());
        });
    }

    public void getStudentsByEmail(String email, final UserArrayCallback callback) {
        ArrayList<User> arr = new ArrayList<>();
        getStudentsEmailByEmail(email, emails -> {
            for (String i : emails) {
                getUser(i, userData -> {
                    arr.add(userData);
                    if (arr.size() == emails.size())
                        callback.onUserArrayFetch(arr);
                });
            }

        });
    }

    public void updateTeacherByNewStudent(String email, User newuser) {
        Log.d(TAG, email);
        addNotificationForUser("Пользователь " + email + " добавил вас к своим ученикам", newuser.email, "Уведомления");
        mDatabase.child("users").child(translate(email)).child("students").
                child(translate(newuser.email)).setValue(translate(newuser.email));
    }

    public void getNotificationByEmail(String email, final NotificationArrayCallback callback) {
        email = translate(email);
        getUser(email, v -> {
            if (v != null) {
                callback.onNotificationArrayFetch(v.notifications);
            } else
                callback.onNotificationArrayFetch(new ArrayList<>());
        });
    }

    public void checkUserInStudentsByEmail(String email, User userData, BoolCallback callback) {
        getUser(email, v -> {
            if (v != null) {
                for (String user : v.students) {
                    if (Objects.equals(user, userData.getEmail())) {
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
        mDatabase.child("users").child(translate(teacher)).child("students").
                child(translate(email)).removeValue();
        addNotificationForUser("Преподаватель " + teacher + " удалил вас, как своего студента.", email, "Уведомления");
    }

    public void deleteNotifyOfUser(Notification data, String user) {
        mDatabase.child("users").child(translate(user)).
                child("notifications").child(data.id_notifications).removeValue();

    }

    public void addNotificationForUser(String text, String email, String title) {
        Notification notification = new Notification(text, mDatabase.child("users").child(translate(email)).child("notifications")
                .push().getKey());
        getUser(email, v->MessageControl.sendMessage(title, text, v.token, context));
        mDatabase.child("users").child(translate(email)).child("notifications")
                .child(notification.id_notifications).setValue(notification);
    }

    public String addCourse(String name, String email) {
        email = translate(email);
        String key = mDatabase.child("course").push().getKey();
        Course course = new Course(name, key);
        mDatabase.child("course").child(key).setValue(course); // Добавляем новый курс с использованием сгенерированного ключа
        ArrayList<String> arr = new ArrayList<>();
        arr.add(key);
        // Добавляем новый курс в список курсов пользователя, используя сгенерированный ключ
        mDatabase.child("users").child(email).child("courses").child(key).setValue(key);
        return key;
    }

    public void getNameCoursesOnUser(String email, ArrayStringCallback callback) {
        email = translate(email);

        // Добавляем слушатель для узла "courses" пользователя
        mDatabase.child("users").child(email).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Перебираем все дочерние элементы (курсы) и добавляем их в список
                    ArrayList<String> courses = new ArrayList<>();
                    for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                        String courseId = courseSnapshot.getValue(String.class);
                        courses.add(courseId);
                    }
                    callback.onArrayStringFetch(courses);
                } else {
                    callback.onArrayStringFetch(new ArrayList<String>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onArrayStringFetch(new ArrayList<String>());
            }
        });
    }

    public void getCoursesOnUser(User user, CoursesCallback callback) {
        ArrayList<Course> arr = new ArrayList<>();
        getNameCoursesOnUser(user.email, courses -> {
            for (String i :
                    courses) {
                getCourse(i, course -> {
                    arr.add(course);
                    if (arr.size() == courses.size()) {
                        callback.onArrayCourseFetch(arr);
                    }
                });
            }
        });
    }


    public void getCourse(String id, final CourseCallback callback) {
        mDatabase.child("course").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() == null) {
                    Log.e("firebase", "Ошибка при получении данных", task.getException());
                    callback.onCourseFetch(null); // Уведомляем обратный вызов о получении нулевого значения
                } else {
                    Object userData = task.getResult().getValue();
                    Log.d("firebase", String.valueOf(userData));
                    Course course = new Course((HashMap<String, Object>) userData);
                    callback.onCourseFetch(course); // Уведомляем обратный вызов о получении данных пользователя
                }
            }
        });
    }

    public void set_like_item_course(String email, String string) {
        mDatabase.child("users").child(translate(email)).child("like_course").setValue(string);
    }

    public void addUserOnCourse(User user, String id) {
        String email = translate(user.email);
        mDatabase.child("course").child(id).child("students").child(email).setValue(true);
        mDatabase.child("users").child(email).child("courses").child(id).setValue(id);
        addNotificationForUser("Вам открыли доступ к новому курсу", user.email, "Уведомления");
    }

    public void deleteUserOnCourse(User user, String id) {
        mDatabase.child("course").child(id).child("students").child(translate(user.email)).removeValue();
        mDatabase.child("users").child(translate(user.email)).child("courses").child(id).removeValue();
        addNotificationForUser("Вам закрыли доступ к курсу", user.email, "Уведомления");
    }

    public void deleteAllNotifyOfUser(String email) {
        mDatabase.child("users").child(translate(email)).
                child("notifications").removeValue();
    }

    public void updateCourseOnNewNote(String title, String id_course) {
        mDatabase.child("course").child(id_course).child("items").push().setValue(title);
    }

    public void setToken(String email, String token) {
        mDatabase.child("users").child(translate(email)).child("token").setValue(token);
    }

    public void removeToken(String email) {
        mDatabase.child("users").child(translate(email)).
                child("token").removeValue();
    }
}
