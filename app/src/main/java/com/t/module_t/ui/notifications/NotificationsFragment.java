package com.t.module_t.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.Notification;
import com.t.module_t.database.NotificationArrayCallback;
import com.t.module_t.databinding.FragmentNotificationsBinding;
import com.t.module_t.ui.notifications.listener.OnStartDragListener;
import com.t.module_t.ui.notifications.listener.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements OnStartDragListener {
    private final String TAG = "NotificationsFragment";
    private ItemTouchHelper mItemTouchHelper;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d(TAG, "create");

        DataBaseControl control = new DataBaseControl(root.getContext());

        RecyclerView recyclerView = root.findViewById(R.id.rec_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ArrayList<Notification> array = new ArrayList<>();

        NotificationAdapter adapter = new NotificationAdapter(getContext(), this, array);
        recyclerView.setAdapter(adapter);
        ImageButton all_delete_button = root.findViewById(R.id.image_button_app_bar_notify_delete_all);
        all_delete_button.setOnClickListener(v ->{
            control.deleteAllNotifyOfUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            array.clear();
            adapter.notifyDataSetChanged();
        });
        control.getNotificationByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), new NotificationArrayCallback() {
            @Override
            public void onNotificationArrayFetch(ArrayList<Notification> notifications) {
                array.addAll(notifications);
                adapter.notifyDataSetChanged();
            }
        });
        control.addNotificationOnProcess(FirebaseAuth.getInstance().getCurrentUser().getEmail(), v ->{
            array.clear();
            array.addAll(v);
            adapter.notifyDataSetChanged();
        });
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onStartDrag(NotificationAdapter.ItemViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}