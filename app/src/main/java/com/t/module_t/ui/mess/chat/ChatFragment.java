package com.t.module_t.ui.mess.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.R;
import com.t.module_t.database.entity.Chat;
import com.t.module_t.database.control.DataBaseControl;
import com.t.module_t.database.control.MessageControl;
import com.t.module_t.databinding.ChatFragmentBinding;
import com.t.module_t.ui.optionally.DownlandFragment;
import com.t.module_t.ui.mess.MessFragment;

import java.util.Objects;

public class ChatFragment extends Fragment {
    private Chat chat;
    private ChatFragmentBinding binding;
    private View root;

    public ChatFragment(Chat chat){
        this.chat = chat;
    }
    void updateUI(){
        ImageView back = root.findViewById(R.id.imageView3);
        DataBaseControl control = new DataBaseControl();
        back.setOnClickListener(v ->{
            DownlandFragment downlandFragment = new DownlandFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerView, downlandFragment);
            fragmentTransaction.commit();
            // Запускаем загрузку данных асинхронно
            Thread thread = new Thread(() -> {
                // Делаем паузу для имитации загрузки данных
                // Заменяем DownlandFragment на целевой фрагмент
                downlandFragment.getNewFragment(this.getContext(), MessFragment.class.getName(), fragment->{
                    if (fragment == null)
                        return;
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragment);
                    transaction.commit();
                });
            });
            thread.start();
        });
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView2);
        ChatAdapter adapter = new ChatAdapter(this.getContext(), chat.messages,
                FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ImageView send = root.findViewById(R.id.imageView4);
        EditText editText = root.findViewById(R.id.editTextText3);
        control.addChatOnProcess(chat.getEmail_teacher(), chat.getEmail_student(), array ->{
            chat.messages.clear();
            adapter.mItems = array;
            adapter.notifyDataSetChanged();
        });
        send.setOnClickListener(v ->{
            if (editText.getText().toString().replace(" ", "").isEmpty())
                return;
            control.sendMessage(chat.getEmail_teacher(), chat.getEmail_student(),
                    editText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
            String s = editText.getText().toString();
            if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail(), chat.getEmail_student())){
                control.getUser(chat.getEmail_teacher(), user ->{
                    MessageControl.sendMessage("Новое сообщение", s, user.token, this.getContext());
                });
            }else {
                control.getUser(chat.getEmail_student(), user ->{
                    MessageControl.sendMessage("Новое сообщение", s, user.token, this.getContext());
                });
            }
            editText.setText("");
        });

        recyclerView.setAdapter(adapter);
        TextView textView = root.findViewById(R.id.textView15);
        textView.setText(chat.getEmail_student());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChatFragmentBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        updateUI();
        return root;
    }


}
