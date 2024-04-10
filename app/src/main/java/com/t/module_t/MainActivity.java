package com.t.module_t;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.databinding.ActivityMainBinding;
import com.t.module_t.ui.cours.CourseFragment;
import com.t.module_t.ui.notifications.NotificationsFragment;
import com.t.module_t.ui.settings.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    ImageButton view0, view1, view2, view3;
    TestFirebaseMessagingService testFirebaseMessagingService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(new NotificationChannel("1011",
                "notify", NotificationManager.IMPORTANCE_LOW));
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        Intent intent = new Intent(this, TestFirebaseMessagingService.class);
        startService(intent);
        view0 = findViewById(R.id.view0);
        view0.setOnClickListener(v -> set_fragment(new Fragment(), view0));
        view1 = findViewById(R.id.view1);
        view1.setOnClickListener(v -> set_fragment(new CourseFragment(), view1));
        view2 = findViewById(R.id.view2);
        view2.setOnClickListener(v -> set_fragment(new NotificationsFragment(), view2));
        view3 = findViewById(R.id.view3);
        view3.setOnClickListener(v -> set_fragment(new ProfileFragment(), view3));
        set_fragment(fragment, view0);
    }

    void set_fragment(Fragment obj, @NonNull ImageButton view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, obj).commit();
        view0.getBackground().setTint(getColor(R.color.black));
        view1.getBackground().setTint(getColor(R.color.black));
        view2.getBackground().setTint(getColor(R.color.black));
        view3.getBackground().setTint(getColor(R.color.black));
        view.getBackground().setTint(getColor(R.color.blue));

    }
}