package com.t.module_t.ui.mess;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.entity.Chat;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.entity.User;
import com.t.module_t.databinding.MessFragmentBinding;

import java.util.ArrayList;


public class MessFragment extends Fragment {
    private final String TAG = "HomeFragment";
    private int flag = 0;
    DataBaseControl control;
    User user;
    View root;
    ArrayList<Chat> array;
    private MessFragmentBinding binding;
    private class LoadMessTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            control = new DataBaseControl();
            array = new ArrayList<>();
            control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData -> {
                user = userData;
                flag++;
            });
            control.getChatsOfUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), chats -> {
                array.addAll(chats);
                flag++;
            });
            while (flag < 2){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Выполняется после завершения загрузки данных
            Log.i(TAG, "constructor");
            flag = 2;
            // После выполнения загрузки данных обновите интерфейс, если это необходимо
        }
    }

    public MessFragment(){
        long startTime = System.currentTimeMillis();
        MessFragment.LoadMessTask loadNotificationTask = new MessFragment.LoadMessTask();
        loadNotificationTask.execute();
        while (flag < 2 && System.currentTimeMillis() - startTime < 5000){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void updateUI(){
        Log.i(TAG, "updateUI");
        RecyclerView view = root.findViewById(R.id.rec_message_common);
        MessAdapter adapter = new MessAdapter(getContext(), array, user, getFragmentManager());
        Log.i(TAG, String.valueOf(adapter.getItemCount()));
        view.setAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
// менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        Log.i(TAG, "create");
        binding = MessFragmentBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        updateUI();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}