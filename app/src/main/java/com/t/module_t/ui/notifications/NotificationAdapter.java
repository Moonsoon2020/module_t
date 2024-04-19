package com.t.module_t.ui.notifications;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.entity.Notification;
import com.t.module_t.ui.notifications.listener.ItemTouchHelperViewHolder;
import com.t.module_t.ui.notifications.listener.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemViewHolder>
         {

    private List<Notification> mItems = new ArrayList<>();
    private final OnStartDragListener mDragStartListener;
    private Context context;
    public Context getContext(){
        return context;
    }
    public NotificationAdapter(Context context, OnStartDragListener dragStartListener, List<Notification> mItems) {
        mDragStartListener = dragStartListener;
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public NotificationAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_fragment, parent, false);
        NotificationAdapter.ItemViewHolder itemViewHolder = new NotificationAdapter.ItemViewHolder(view);

        return itemViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ItemViewHolder holder, int position) {
        Notification notification = mItems.get(position);
        holder.data = notification;
        holder.textView.setText(notification.text);
        holder.dateView.setText(notification.date.toString());
    }


    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);

    }

     public List<Notification> getmItems() {
         return mItems;
     }

     public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final TextView dateView;
        public Notification data;


        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView_fragment_notify);
            dateView = (TextView) itemView.findViewById(R.id.textView_fragment_notify_data);

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
