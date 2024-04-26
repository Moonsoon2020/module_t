package com.t.module_t.database.control;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.t.module_t.database.callbacks.BoolCallback;
import com.t.module_t.database.callbacks.FileCallback;
import com.t.module_t.database.entity.FileT;

import java.io.File;
import java.io.IOException;

public class StorageControl {
    private StorageReference storage;
    private final String TAG = "StorageControl";
    private String id_course;
    private DataBaseControl control;

    public StorageControl(String id_course) {
        storage = FirebaseStorage.getInstance().getReference().child(id_course);
        control = new DataBaseControl();
        this.id_course = id_course;
    }

    private String translate(String email) {
        return email.replace(".", "~");
    }

    public void getFile(String name, FileCallback callback) throws IOException {
        storage.child(name).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata metadata) {
                File file = null;

                try {
                    file = File.createTempFile(id_course, metadata.getContentType().replace("application/", ""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                File finalFile = file;
//                storage.child(name).getFile(file).addOnCompleteListener(v -> {
                    callback.onFileFetch(new FileT(finalFile, metadata.getContentType()));
//                });
            }

        });

    }

    public void addFile(Uri fileUri, String string, BoolCallback callback) {
        storage.child(string).putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> { // попробуй наоборот
                    control.updateCourseOnNewNote(string, id_course);
                    callback.onBoolFetch(true);
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, exception.toString());
                    callback.onBoolFetch(false);

                });
    }

    public void deleteFile(String name){
        storage.child(name).delete();
    }


}
