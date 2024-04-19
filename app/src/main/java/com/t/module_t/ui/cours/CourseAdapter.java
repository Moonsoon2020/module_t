package com.t.module_t.ui.cours;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ActivityNotFoundException;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.database.entity.CourseElement;
import com.t.module_t.database.control.StorageControl;
import com.t.module_t.database.entity.User;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "CourseAdapter";
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
                    StorageControl control = new StorageControl(id_course);
                    try {
                        control.getFile(hold.nameView.getText().toString(), file -> {
                            Uri fileUri = FileProvider.getUriForFile(
                                    inflater.getContext(),
                                    inflater.getContext().getApplicationContext().getPackageName() + ".provider",
                                    file.file);
                            Log.d(TAG, file.type.toString());
                            Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                            openFileIntent.setDataAndType(fileUri, file.type); // здесь могут быть любые файлы
                            openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            try {
                                inflater.getContext().startActivity(openFileIntent);
                            } catch (ActivityNotFoundException ignored){
                                Toast.makeText(inflater.getContext(), "Стандартное средство открытия файла этого типа не найдено", Toast.LENGTH_SHORT).show();
                            }

                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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

    private String getMimeTypeFromFile(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(file.getName());
        return mimeType;
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