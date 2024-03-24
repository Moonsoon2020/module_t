package com.t.module_t.ui.cours;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.CreateNote;
import com.t.module_t.R;
import com.t.module_t.database.CourseElement;
import com.t.module_t.database.User;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<CourseElement> states;
    private final User user;
    private String id_course;

    public CourseAdapter(Context context, List<CourseElement> states, User user, String id_course) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.user = user;
        this.id_course = id_course;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == states.size() && user.status)
            return 1;
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_element_item,
                        parent, false);
                return new ViewHolderItem(view);
            }
            case 1: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_element_end,
                        parent, false);
                return new ViewHolderEnd(view);
            }
        }

        try {
            throw new Exception("Не указан тип движимого объекта");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:{
                ViewHolderItem hold = (ViewHolderItem) holder;
                CourseElement state = states.get(position);
                hold.nameView.setText(state.getName());
                hold.button.setOnClickListener(v -> {
                    if (user.status) {

                    } else {

                    }
                });
                break;
            }
            case 1:{
                ViewHolderEnd hold = (ViewHolderEnd) holder;
                hold.button.setOnClickListener(v ->{
                    Intent intent = new Intent(inflater.getContext(), CreateNote.class);
                    intent.putExtra("id_course", id_course);
                    inflater.getContext().startActivity(intent);
                });
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (user.status)
            return states.size() + 1;
        return states.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        final TextView nameView;
        ImageButton button;

        ViewHolderItem(View view) {
            super(view);
            nameView = view.findViewById(R.id.textView_course);
            button = view.findViewById(R.id.imageButton2);
        }
    }

    public static class ViewHolderEnd extends RecyclerView.ViewHolder {
        Button button;
        ViewHolderEnd(View view) {
            super(view);
            button = view.findViewById(R.id.button2);
        }
    }
}