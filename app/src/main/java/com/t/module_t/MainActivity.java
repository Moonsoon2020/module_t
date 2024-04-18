package com.t.module_t;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.databinding.ActivityMainBinding;
import com.t.module_t.ui.DownlandFragment;
import com.t.module_t.ui.cours.CourseFragment;
import com.t.module_t.ui.mess.MessFragment;
import com.t.module_t.ui.notifications.NotificationsFragment;
import com.t.module_t.ui.settings.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    ImageButton view0, view1, view2, view3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(new NotificationChannel("1011",
                "notify", NotificationManager.IMPORTANCE_LOW));
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        Intent intent = new Intent(this, FirebaseMessagingService.class);
        startService(intent);
        view0 = findViewById(R.id.view0);
        view0.setBackgroundColor(getColor(R.color.white));
        view0.setOnClickListener(v -> set_fragment(MessFragment.class, view0));
        view1 = findViewById(R.id.view1);
        view1.setBackgroundColor(getColor(R.color.white));
        view1.setOnClickListener(v -> set_fragment(CourseFragment.class, view1));
        view2 = findViewById(R.id.view2);
        view2.setBackgroundColor(getColor(R.color.white));
        view2.setOnClickListener(v -> set_fragment(NotificationsFragment.class, view2));
        view3 = findViewById(R.id.view3);
        view3.setBackgroundColor(getColor(R.color.white));
        view3.setOnClickListener(v -> set_fragment(ProfileFragment.class, view3));
        set_fragment(CourseFragment.class, view1);
    }

    void set_fragment(Class<? extends Fragment> obj, @NonNull ImageButton view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        view0.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view.setColorFilter(ContextCompat.getColor(this, R.color.blue), PorterDuff.Mode.SRC_IN);

        // Отображаем DownlandFragment
        DownlandFragment downlandFragment = new DownlandFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, downlandFragment);
        fragmentTransaction.commit();
        // Устанавливаем цвет фона кнопок


        // Запускаем загрузку данных асинхронно
        Thread thread = new Thread(() -> {
            // Делаем паузу для имитации загрузки данных
            // Заменяем DownlandFragment на целевой фрагмент
            downlandFragment.getNewFragment(this, obj.getName(), fragment->{
                if (fragment == null)
                    return;
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, fragment);
                transaction.commit();
            });
        });
        thread.start();
    }
}