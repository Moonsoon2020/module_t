package com.t.module_t.ui.cours;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.t.module_t.R;
import com.t.module_t.database.control.StorageControl;

import org.greenrobot.eventbus.EventBus;

public class CreateNote extends AppCompatActivity {
    private String TAG = "CreateNote";
    private Uri chosenUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note_course);
        EditText editText = findViewById(R.id.editTextText2);
        ImageButton button = findViewById(R.id.imageButton3);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/*");
            startActivityForResult(intent, 1);
        });
        Button button_start = findViewById(R.id.button3);
        button_start.setOnClickListener(v ->{
            if (editText.getText().toString().length() < 4){
                return;
            }
            button_start.setEnabled(false);
            StorageControl control = new StorageControl(getIntent().getStringExtra("id_course"));

            control.addFile(chosenUri, editText.getText().toString(), flag -> {
                Log.d(TAG, String.valueOf(flag));
                EventBus.getDefault().post(editText.getText().toString());
                finish();
            });
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    chosenUri = data.getData();
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageResource(R.drawable.baseline_check_24);
                }
                break;
            }
        }
    }
}
