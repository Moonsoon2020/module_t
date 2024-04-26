package com.t.module_t.ui.cours;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.t.module_t.R;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.control.StorageControl;

import java.io.IOException;

public class ChangeNode extends AppCompatActivity {
    private String TAG = "ChangeNode";
    String name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_node_fragment);
        String id_course =  getIntent().getStringExtra("id_course");
        name = getIntent().getStringExtra("name");
        TextView textView = findViewById(R.id.textView40);
        textView.setText(name);
        ImageButton button = findViewById(R.id.imageButton5);
        button.setOnClickListener(v -> {
            StorageControl control = new StorageControl(id_course);
            try {
                control.getFile(textView.getText().toString(), file -> {
                    Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file.file);
                    Log.d(TAG, file.type.toString());
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.setDataAndType(fileUri, file.type); // здесь могут быть любые файлы
                    openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        this.startActivity(openFileIntent);
                    } catch (ActivityNotFoundException ignored) {
                        Toast.makeText(this, "Стандартное средство открытия файла этого типа не найдено", Toast.LENGTH_SHORT).show();
                    }

                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ImageButton imageButton = findViewById(R.id.imageButton5);
        imageButton.setOnClickListener(v ->{
            DataBaseControl control = new DataBaseControl();
            control.deleteNodeOnCourse(textView.getText().toString(), id_course);
            StorageControl storageControl = new StorageControl(id_course);
            storageControl.deleteFile(name);
            finish();
        });
    }
}
