package com.t.module_t.database;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class StorageControl {
    private StorageReference storage;
    private final String TAG = "StorageControl";
    private String email;
    private String id_course;
    private DataBaseControl control;

    public StorageControl(String email, String id_course) {
        this.email = translate(email);
        storage = FirebaseStorage.getInstance().getReference().child(this.email);
        control = new DataBaseControl();
        this.id_course = id_course;
    }

    private String translate(String email) {
        return email.replace(".", "~");
    }

    public void getFile(String name, FileCallback callback) throws IOException {
        storage.child(id_course).child(name).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata metadata) {
                File file = null;

                try {
                    file = File.createTempFile(id_course, metadata.getContentType().replace("application/", ""));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                File finalFile = file;
                storage.child(id_course).child(name).getFile(file).addOnCompleteListener(v -> {
                    callback.onFileFetch(new FileT(finalFile, metadata.getContentType()));
                });
            }
        });

    }

    public void addFile(Uri fileUri, String string, BoolCallback callback) {
        storage.child(id_course).child(string).putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    control.updateCourseOnNewNote(string, email, id_course);
                    callback.onBoolFetch(true);
                })
                .addOnFailureListener(exception -> {
                    callback.onBoolFetch(false);
                });
    }
}
