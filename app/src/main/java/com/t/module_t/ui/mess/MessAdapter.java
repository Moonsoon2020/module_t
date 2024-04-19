package com.t.module_t.ui.mess;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.entity.Chat;
import com.t.module_t.database.entity.User;
import com.t.module_t.ui.mess.chat.ChatFragment;

import java.util.ArrayList;
import java.util.List;

public class MessAdapter extends RecyclerView.Adapter<MessAdapter.ItemViewHolder> {

    public List<Chat> mItems = new ArrayList<>();
    private Context context;
    private FragmentManager manager;
    private User user;

    public Context getContext() {
        return context;
    }

    public MessAdapter(Context context, List<Chat> mItems, User user, FragmentManager manager) {
        this.mItems = mItems;
        this.context = context;
        this.user = user;
        this.manager = manager;
    }

    @NonNull
    @Override
    public MessAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.mess_frag_element, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessAdapter.ItemViewHolder holder, int position) {
        String item;
        if (user.status)
            item = mItems.get(position).getEmail_student();
        else
            item = mItems.get(position).getEmail_teacher();
        holder.textView.setText(item);
        holder.id = translate(mItems.get(position).getEmail_teacher()) +
                translate(mItems.get(position).getEmail_student());
        holder.button.setOnClickListener(v -> {
            manager.beginTransaction().replace(R.id.fragmentContainerView, new ChatFragment(mItems.get(position))).commit();
        });
    }
    private String translate(String email) {
        return email.replace(".", "~");
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final TextView textView;
        public final ImageButton button;
        public String id;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView12);
            button = itemView.findViewById(R.id.imageButton4);
        }
    }
}

