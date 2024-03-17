package com.t.module_t.ui.notifications;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.database.Notification;

import java.util.List;
public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Notification> list;

    public NotificationAdapter(Context context, List<Notification> list) {
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
        holder.textView.setText(notification.text);
        holder.dateView.setText(notification.date.toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView, dateView;
        ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.textView_fragment_notify);
            dateView = view.findViewById(R.id.textView_fragment_notify_data);
        }
    }
}