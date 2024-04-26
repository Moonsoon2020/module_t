package com.t.module_t.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.t.module_t.R;
import com.t.module_t.databinding.ActivityMainBinding;
import com.t.module_t.service.FirebaseMessagingService;
import com.t.module_t.ui.cours.CourseFragment;
import com.t.module_t.ui.entity.EntityFragmentView;
import com.t.module_t.ui.mess.MessFragment;
import com.t.module_t.ui.notifications.NotificationsFragment;
import com.t.module_t.ui.optionally.DownlandFragment;
import com.t.module_t.ui.settings.ProfileFragment;

import java.util.EmptyStackException;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    ImageButton view0, view1, view2, view3;
    Stack<EntityFragmentView> stack = new Stack<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(new NotificationChannel(
                "1011","notify", NotificationManager.IMPORTANCE_LOW));
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        Intent intent = new Intent(this, FirebaseMessagingService.class);
        startService(intent);
        view0 = findViewById(R.id.view0);
        view0.setBackgroundColor(getColor(R.color.white));
        view0.setOnClickListener(v -> set_fragment(new EntityFragmentView(MessFragment.class, view0)));
        view1 = findViewById(R.id.view1);
        view1.setBackgroundColor(getColor(R.color.white));
        view1.setOnClickListener(v -> set_fragment(new EntityFragmentView(CourseFragment.class, view1)));
        view2 = findViewById(R.id.view2);
        view2.setBackgroundColor(getColor(R.color.white));
        view2.setOnClickListener(v -> set_fragment(new EntityFragmentView(NotificationsFragment.class, view2)));
        view3 = findViewById(R.id.view3);
        view3.setBackgroundColor(getColor(R.color.white));
        view3.setOnClickListener(v -> set_fragment(new EntityFragmentView(ProfileFragment.class, view3)));
        set_fragment(new EntityFragmentView(CourseFragment.class, view1));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                set_fragment(null);
            }
        });
    }

    void set_fragment(EntityFragmentView entity) {
        if (entity == null){
            try {
                entity = stack.pop();
            } catch (EmptyStackException e) {
                finish();
            }
        } else {
            stack.add(entity);
        }
        Class<? extends Fragment> obj = entity.getFragmentClass();
        ImageButton view = (ImageButton) entity.getView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Устанавливаем цвет фона кнопок
        view0.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view1.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view2.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view3.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        view.setColorFilter(ContextCompat.getColor(this, R.color.blue), PorterDuff.Mode.SRC_IN);
        DownlandFragment downlandFragment = new DownlandFragment();
        fragmentTransaction.replace(R.id.fragmentContainerView, downlandFragment);
        fragmentTransaction.commit();
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