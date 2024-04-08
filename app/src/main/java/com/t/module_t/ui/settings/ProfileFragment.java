package com.t.module_t.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.Authentication;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.databinding.ProfAcBinding;
import com.t.module_t.ui.settings.student.StudentFragment;


public class ProfileFragment extends Fragment {
    private final String TAG = "SettingsFragment";

    private ProfAcBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ProfAcBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        DataBaseControl control = new DataBaseControl();
        TextView textView = root.findViewById(R.id.textView);
        LinearLayout button_set = root.findViewById(R.id.linearLayout5);
        button_set.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Fragment()).commit();
        });
        LinearLayout button_std = root.findViewById(R.id.linearLayout3);
        button_std.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new StudentFragment()).commit();
        });
        LinearLayout button_pop = root.findViewById(R.id.linearLayout4);
        button_pop.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Fragment()).commit();
        });
        LinearLayout button_help = root.findViewById(R.id.linearLayout6);
        button_help.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Fragment()).commit();
        });
        control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), userData -> {
            textView.setText(userData.username);
            if (userData.status){
                button_std.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout button_out = root.findViewById(R.id.linearLayout0);
        button_out.setOnClickListener(v -> {
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