package com.t.module_t.ui.settings;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.AutoriseActivity;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.databinding.ProfAcBinding;
import com.t.module_t.ui.settings.student.StudentFragment;


public class ProfileFragment extends Fragment {
    private final String TAG = "SettingsFragment";
    private View root;
    private boolean flag = false;
    private ProfAcBinding binding;
    DataBaseControl control;
    User user;
    public ProfileFragment(){
        long startTime = System.currentTimeMillis();
        ProfileFragment.LoadProfileTask loadNotificationTask = new ProfileFragment.LoadProfileTask();
        loadNotificationTask.execute();
        while (!flag && System.currentTimeMillis() - startTime < 5000){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class LoadProfileTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            control = new DataBaseControl();
            control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData -> {
                user = userData;
                flag = true;
            });
            while (!flag){
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
            flag = true;
            // После выполнения загрузки данных обновите интерфейс, если это необходимо
        }
    }
    private void updateUI(){
        TextView textView = root.findViewById(R.id.textView);
        LinearLayout button_set = root.findViewById(R.id.linearLayout5);
        button_set.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new SettingsFragment()).commit();
        });
        LinearLayout button_std = root.findViewById(R.id.linearLayout3);
        button_std.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new StudentFragment()).commit();
        });
        LinearLayout button_pop = root.findViewById(R.id.linearLayout4);
        button_pop.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new POP_fragment()).commit();
        });
        LinearLayout button_help = root.findViewById(R.id.linearLayout6);
        button_help.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HelpFragment()).commit();
        });
        LinearLayout button_out = root.findViewById(R.id.linearLayout0);
        button_out.setOnClickListener(v -> {
            control.removeToken(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            FirebaseAuth.getInstance().signOut();
            Intent mIntent = new Intent(root.getContext(), AutoriseActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
        textView.setText(user.username);
        if (user.status){
            button_std.setVisibility(View.VISIBLE);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ProfAcBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        control.setContext(root.getContext());
        updateUI();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}