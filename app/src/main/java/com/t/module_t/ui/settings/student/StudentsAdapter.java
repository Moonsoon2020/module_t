package com.t.module_t.ui.settings.student;


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

import java.util.List;
public class StudentsAdapter  extends RecyclerView.Adapter<StudentsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<User> list;
    private final String teacher;

    public StudentsAdapter(Context context, List<User> list, String teacher) {
        this.list = list;
        this.teacher = teacher;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_student,parent, false);
        return new StudentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentsAdapter.ViewHolder holder, int position) {
        User user = list.get(position);
        holder.nameView.setText(user.username + " " + user.email);
        holder.button.setOnClickListener(v ->{
            DataBaseControl control = new DataBaseControl(inflater.getContext());
            list.remove(position);
            control.deleteUserOfStudents(teacher, user.getEmail());
            notifyItemRemoved(list.size() - 1);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        ImageView button;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.textViewsettigs_student);
            button = view.findViewById(R.id.imageButton);

        }
    }
}