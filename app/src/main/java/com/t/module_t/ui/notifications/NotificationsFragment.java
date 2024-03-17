package com.t.module_t.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
        RecyclerView recyclerView = root.findViewById(R.id.rec_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ArrayList<Notification> array = new ArrayList<>();
        DataBaseControl control = new DataBaseControl();
        NotificationAdapter adapter = new NotificationAdapter(getContext(), array);
        recyclerView.setAdapter(adapter);
        control.getNotificationByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), new NotificationArrayCallback() {
            @Override
            public void onNotificationArrayFetch(ArrayList<Notification> notifications) {
                array.addAll(notifications);
                adapter.notifyItemChanged(0);
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