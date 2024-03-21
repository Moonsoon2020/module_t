package com.t.module_t.ui.notifications.listener;


import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.ui.notifications.NotificationAdapter;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(NotificationAdapter.ItemViewHolder viewHolder);

}
