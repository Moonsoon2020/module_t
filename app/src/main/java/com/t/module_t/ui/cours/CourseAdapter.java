package com.t.module_t.ui.cours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.CourseElement;
import com.t.module_t.database.User;

import java.util.List;
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CourseElement> states;
    private User user;

    public CourseAdapter(Context context, List<CourseElement> states, User user) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.user = user;
    }
    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_element_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position) {
        CourseElement state = states.get(position);
        holder.nameView.setText(state.getName());
        holder.button.setOnClickListener(v -> {
            if (user.status) {

            } else {

            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        ImageButton button;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.textView_course);
            button = view.findViewById(R.id.imageButton2);
        }
    }
}