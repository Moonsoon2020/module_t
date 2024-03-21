package com.t.module_t.ui.notifications.listener;

import androidx.annotation.NonNull;

import com.t.module_t.ui.notifications.NotificationAdapter;

public interface ItemTouchListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onBindViewHolder(@NonNull NotificationAdapter.ItemViewHolder holder, int position);

    void onItemDismiss(int position);

}
