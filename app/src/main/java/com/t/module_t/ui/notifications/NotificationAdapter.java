package com.t.module_t.ui.notifications;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.database.Notification;
import com.t.module_t.listener.OnSwipeTouchListener;

import java.util.List;
public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Notification> list;
    private final String TAG = "NotificationAdapter";
    private final String user;

    public NotificationAdapter(Context context, List<Notification> list, String user) {
        this.user = user;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public com.t.module_t.ui.notifications.NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_fragment ,parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = list.get(position);
        holder.data = notification;
        holder.textView.setText(notification.text);
        holder.dateView.setText(notification.date.toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView, dateView;
        private Notification data;

        ViewHolder(View view){
            super(view);
            DataBaseControl control = new DataBaseControl();
            textView = view.findViewById(R.id.textView_fragment_notify);
            dateView = view.findViewById(R.id.textView_fragment_notify_data);
            view.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
                public void onSwipeTop() {
//                    Toast.makeText(view.getContext(), "top", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeRight() {
//                    Toast.makeText(view.getContext(), "right", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeLeft() {
                    list.remove(data);
                    control.deleteNotifyOfUser(list, user);
                    notifyDataSetChanged();
                    Toast.makeText(view.getContext(), "left", Toast.LENGTH_SHORT).show();
                }
                public void onSwipeBottom() {
//                    Toast.makeText(view.getContext(), "bottom", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }
}