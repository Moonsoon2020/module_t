package com.t.module_t.ui.mess.chat;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.entity.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {

    public List<Message> mItems = new ArrayList<>();
    private Context context;
    String me;


    public ChatAdapter(Context context,List<Message> messages, String me) {
        this.mItems = messages;
        this.me = me;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.mess_element, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ItemViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position).text);
        if (!Objects.equals(mItems.get(position).by, me))
            holder.textView.setGravity(Gravity.END);
    }
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView14);
        }
    }
}

