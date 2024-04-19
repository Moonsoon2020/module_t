package com.t.module_t.ui.cours.setting;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.entity.User;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<User> list;
    private final String id_course;
    private DataBaseControl control;

    public StudentsAdapter(Context context, List<User> list, String id_course) {
        this.list = list;
        this.id_course = id_course;
        this.inflater = LayoutInflater.from(context);
        this.control = new DataBaseControl(context);
    }
    @NonNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.settings_course_item,parent, false);
        return new StudentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentsAdapter.ViewHolder holder, int position) {
        User user = list.get(position);
        holder.nameView.setText(user.username + " " + user.email);
        holder.aSwitch.setChecked(check_id(user));
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.aSwitch.isChecked()){
                    control.addUserOnCourse(user, id_course);
                } else {
                    control.deleteUserOnCourse(user, id_course);
                }
            }
        });
    }
    private boolean check_id(User user){
        for (String string:user.id_courses) {
            if (string.equals(id_course)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        Switch aSwitch;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.textView2);
            aSwitch = view.findViewById(R.id.switch1);

        }
    }
}