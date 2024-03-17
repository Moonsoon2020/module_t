package com.t.module_t.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.t.module_t.Authentication;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.databinding.FragmentSettingBinding;



public class SettingsFragment extends Fragment {
    private final String TAG = "SettingsFragment";

    private FragmentSettingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle bundle = new Bundle();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle arguments = new Bundle();
        DataBaseControl control = new DataBaseControl();
        control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), v->{
            if (((User)v).status){
                Fragment fragOne = new TeacherSettingsFragment();
                fragOne.setArguments(arguments);
                ft.add(R.id.fragment_settings, fragOne);
            } else{

            }
            ft.commit();
        });
        Button button = root.findViewById(R.id.buttonSet);
        button.setOnClickListener(v ->{
            FirebaseAuth.getInstance().signOut();
            Intent mIntent = new Intent(root.getContext(), Authentication.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });


//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}