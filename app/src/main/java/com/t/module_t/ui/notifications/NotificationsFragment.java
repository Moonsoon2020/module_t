package com.t.module_t.ui.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.entity.Notification;
import com.t.module_t.databinding.FragmentNotificationsBinding;
import com.t.module_t.ui.notifications.listener.OnStartDragListener;
import com.t.module_t.ui.notifications.listener.SimpleItemTouchHelperCallback;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements OnStartDragListener {
    private final String TAG = "NotificationsFragment";
    private ItemTouchHelper mItemTouchHelper;
    public boolean flag = false;

    private FragmentNotificationsBinding binding;
    private DataBaseControl control;
    ArrayList<Notification> array;
    View root;

    private class LoadNotificationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            control = new DataBaseControl();
            array = new ArrayList<>();
            control.getNotificationByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), notifications -> {
                array.addAll(notifications);
                flag = true;
            });
            while (!flag) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Выполняется после завершения загрузки данных
            Log.i(TAG, "constructor");
            flag = true;
            // После выполнения загрузки данных обновите интерфейс, если это необходимо
        }
    }

    private void updateUI() {
        RecyclerView recyclerView = root.findViewById(R.id.rec_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        NotificationAdapter adapter = new NotificationAdapter(getContext(), this, array);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        ImageButton all_delete_button = root.findViewById(R.id.image_button_app_bar_notify_delete_all);
        all_delete_button.setOnClickListener(v -> {
            control.deleteAllNotifyOfUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            array.clear();
            adapter.notifyDataSetChanged();
        });

        control.addNotificationOnProcess(FirebaseAuth.getInstance().getCurrentUser().getEmail(), v -> {
            array.clear();
            array.addAll(v);
            adapter.notifyDataSetChanged();
        });
    }

    public NotificationsFragment() {
        long startTime = System.currentTimeMillis();
        NotificationsFragment.LoadNotificationTask loadNotificationTask = new NotificationsFragment.LoadNotificationTask();
        loadNotificationTask.execute();
        while (!flag && System.currentTimeMillis() - startTime < 5000) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        control.setContext(root.getContext());
        Log.d(TAG, "create");
        updateUI();
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