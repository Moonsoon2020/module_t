package com.t.module_t.ui.cours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;

import java.util.List;
public class CourseAdapter  extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CourseElement> states;

    public CourseAdapter(Context context, List<CourseElement> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_fragmen,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position) {
        CourseElement state = states.get(position);
        holder.nameView.setText(state.getName());
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.textView_course);

        }
    }
}