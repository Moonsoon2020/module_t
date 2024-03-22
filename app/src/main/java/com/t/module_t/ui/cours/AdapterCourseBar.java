package com.t.module_t.ui.cours;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCourseBar extends ArrayAdapter<String> {
    private Context context;
    private int textViewResourceId;
    private ArrayList<String> objects;
    public AdapterCourseBar(Context context, int textViewResourceId,
                         ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, textViewResourceId, null);

        TextView tv = (TextView) convertView;
        tv.setText(objects.get(position));

        return convertView;
    }
}