package com.t.module_t.ui.notifications;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.Notification;
import com.t.module_t.database.NotificationArrayCallback;
import com.t.module_t.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    private final String TAG = "NotificationsFragment";

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d(TAG, "create");
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setCustomView(R.layout.notification_toolbar);
        bar.setDisplayShowCustomEnabled(true);
        ImageButton all_delete_button = bar.getCustomView().findViewById(R.id.image_button_app_bar_notify_delete_all);
        all_delete_button.setOnClickListener(v ->{
            Log.d(TAG, "alldelete");
        });
        RecyclerView recyclerView = root.findViewById(R.id.rec_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ArrayList<Notification> array = new ArrayList<>();
        DataBaseControl control = new DataBaseControl();
        NotificationAdapter adapter = new NotificationAdapter(getContext(), array, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        recyclerView.setAdapter(adapter);
        control.getNotificationByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), new NotificationArrayCallback() {
            @Override
            public void onNotificationArrayFetch(ArrayList<Notification> notifications) {
                array.addAll(notifications);
                adapter.notifyDataSetChanged();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}