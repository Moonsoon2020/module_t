package com.t.module_t.database.control;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t.module_t.database.callbacks.ArrayStringCallback;
import com.t.module_t.database.callbacks.BoolCallback;
import com.t.module_t.database.callbacks.CourseCallback;
import com.t.module_t.database.callbacks.CoursesCallback;
import com.t.module_t.database.callbacks.NotificationArrayCallback;
import com.t.module_t.database.callbacks.UserArrayCallback;
import com.t.module_t.database.callbacks.UserCallback;
import com.t.module_t.database.callbacks.UserEmailArrayCallback;
import com.t.module_t.database.entity.Chat;
import com.t.module_t.database.entity.Course;
import com.t.module_t.database.entity.Message;
import com.t.module_t.database.entity.Notification;
import com.t.module_t.database.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
        addNotificationForUser("Привет, поздравляю с регистрацией", email, "Уведомления");
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
                callback.onUserEmailArrayFetch(v.getPeople());
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

    public void updateByNewStudent(String email, User newuser) {
        Log.d(TAG, email);
        if (newuser.status)
            return;
        addNotificationForUser("Пользователь " + email + " добавил вас к своим ученикам", newuser.email, "Уведомления");
        mDatabase.child("users").child(translate(email)).child("students").
                child(translate(newuser.email)).setValue(translate(newuser.email));
        mDatabase.child("users").child(translate(newuser.email)).child("teachers").
                child(translate(email)).setValue(translate(email));
        newChat(email, newuser.email);
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
                for (String user : v.piple) {
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
        mDatabase.child("users").child(translate(email)).child("teachers").
                child(translate(teacher)).removeValue();
        mDatabase.child("message").child(translate(teacher) + translate(email)).removeValue();
        addNotificationForUser("Преподаватель " + teacher + " удалил вас, как своего студента.", email, "Уведомления");
    }

    public void deleteNotifyOfUser(Notification data, String user) {
        mDatabase.child("users").child(translate(user)).
                child("notifications").child(data.id_notifications).removeValue();

    }

    public void addNotificationForUser(String text, String email, String title) {
        Notification notification = new Notification(text, mDatabase.child("users").child(translate(email)).child("notifications")
                .push().getKey());
        getUser(email, v -> MessageControl.sendMessage(title, text, v.token, context));
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

    public void addNotificationOnProcess(String email, NotificationArrayCallback callback) {
        email = translate(email);
        mDatabase.child("users").child(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Log.i(TAG, snapshot.toString() + "\n" + previousChildName);
//                callback.onNotificationFetch(new Notification((HashMap<String, Object>)snapshot.getValue()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, snapshot.toString());
                ArrayList<Notification> list = new ArrayList<>();

                // Преобразуем DataSnapshot в HashMap
                HashMap<String, Object> notificationsMap = (HashMap<String, Object>) snapshot.getValue();
                if (notificationsMap != null) {
                    // Перебираем каждую запись в узле "notifications" и создаем объекты Notification
                    for (Map.Entry<String, Object> entry : notificationsMap.entrySet()) {
                        Notification notification = getNotification(entry);
                        list.add(notification);
                    }
                }
                // Передаем список уведомлений через колбэк
                callback.onNotificationArrayFetch(list);
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "adddddddddddddddddddddddddddddddd");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "adddddddddddddddddddddddddddddddd");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "adddddddddddddddddddddddddddddddd");

            }
        });
    }

    @NonNull
    private static Notification getNotification(Map.Entry<String, Object> entry) {
        HashMap<String, Object> notificationData = (HashMap<String, Object>) entry.getValue();

        // Извлекаем данные уведомления из HashMap
        String id = (String) notificationData.get("id_notifications");
        String text = (String) notificationData.get("text");
        HashMap<String, Object> dateData = (HashMap<String, Object>) notificationData.get("date");

        // Создаем объект Notification и добавляем его в список
        Notification notification = new Notification(text, id, dateData);
        return notification;
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

    public String updateCourseOnNewNote(String title, String id_course) {
        String key = String.valueOf(mDatabase.child("course").child(id_course).child("items").push().getKey());
        mDatabase.child("course").child(id_course).child("items").child(key).setValue(title);
        return key;
    }

    public void setToken(String email, String token) {
        mDatabase.child("users").child(translate(email)).child("token").setValue(token);
    }

    public void removeToken(String email) {
        mDatabase.child("users").child(translate(email)).
                child("token").removeValue();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void newChat(String email_teacher, String email_student) {
        String key = translate(email_teacher) + translate(email_student);
        Chat chat = new Chat(email_teacher, email_student);
        mDatabase.child("message").child(key).setValue(chat);
        newMessageOnChat(key, email_teacher);
    }

    public void newMessageOnChat(String key, String email) {
        mDatabase.child("message").child(key).child("messages").push()
                .setValue(new Message("привет, я твой новый препод", email));
    }

    public void sendMessage(String emailTeacher, String emailStudent, String string, String by) {
        mDatabase.child("message").child(translate(emailTeacher) + translate(emailStudent))
                .child("messages").push().setValue(new Message(string, by));
    }

    public void changeNameNodeByIDCourse(String idCourse, String new_name, String old_name) {
        mDatabase.child("course").child(idCourse).child("items").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> chatData = (HashMap<String, String>) task.getResult().getValue();
                assert chatData != null;
                for (String id : chatData.keySet()) {
                    if (Objects.equals(chatData.get(id), old_name)) {
                        mDatabase.child("course").child(idCourse).child("items").child(id).setValue(new_name);
                        break;
                    }
                }
            }
        });

    }

    public void deleteNodeOnCourse(String name, String idCourse) {
        mDatabase.child("course").child(idCourse).child("items").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> chatData = (HashMap<String, String>) task.getResult().getValue();
                assert chatData != null;
                for (String id : chatData.keySet()) {
                    if (Objects.equals(chatData.get(id), name)) {
                        mDatabase.child("course").child(idCourse).child("items").child(id).removeValue();
                        break;
                    }
                }
            }
        });
    }

    public interface ArrayMessageCallback {
        void onArrayMessageOnFetch(ArrayList<Message> messages);
    }

    public void addChatOnProcess(String emailTeacher, String emailStudent, ArrayMessageCallback callback) {
        mDatabase.child("message").child(translate(emailTeacher) + translate(emailStudent)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Log.i(TAG, snapshot.toString() + "\n" + previousChildName);
//                callback.onNotificationFetch(new Notification((HashMap<String, Object>)snapshot.getValue()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, snapshot.toString());
                ArrayList<Message> list = new ArrayList<>();

                // Преобразуем DataSnapshot в HashMap
                HashMap<String, Object> notificationsMap = (HashMap<String, Object>) snapshot.getValue();
                if (notificationsMap != null) {
                    // Перебираем каждую запись в узле "notifications" и создаем объекты Notification
                    for (Map.Entry<String, Object> entry : notificationsMap.entrySet()) {
                        Message message = new Message((HashMap<String, Object>) entry.getValue());
                        list.add(message);
                    }
                }
                // Передаем список уведомлений через колбэк
                Collections.sort(list);
                callback.onArrayMessageOnFetch(list);
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "adddddddddddddddddddddddddddddddd");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "adddddddddddddddddddddddddddddddd");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "adddddddddddddddddddddddddddddddd");

            }
        });
    }

    public interface ArrayChatCallback {

        void onArrayChatFetch(ArrayList<Chat> chats);

    }

    public void getChatsOfUser(String email, ArrayChatCallback callback) {
        getUser(email, user -> {
            if (user == null || user.piple.isEmpty()) {
                callback.onArrayChatFetch(null);
                return;
            }
            ArrayList<Chat> chats = new ArrayList<>();
            String token;
            for (String email_ :
                    user.piple) {
                if (user.status) {
                    token = translate(email) + translate(email_);
                } else {
                    token = translate(email_) + translate(email);
                }
                mDatabase.child("message").child(token).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().getValue() != null) {
                            Object chatData = task.getResult().getValue();
                            Log.d("firebase", String.valueOf(chatData));
                            chats.add(new Chat((HashMap<String, Object>) chatData));
                            if (chats.size() == user.piple.size()) {
                                callback.onArrayChatFetch(chats);
                            }
                        }
                    }
                });
            }
        });
    }
}
