package com.t.module_t.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.t.module_t.R;
import com.t.module_t.databinding.HelpFragmentBinding;

public class HelpFragment extends Fragment {

    private HelpFragmentBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HelpFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView textView = root.findViewById(R.id.back2);
        textView.setOnClickListener(v ->
                on_back());
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                on_back();
            }
        });
        return root;
    }
    private void on_back() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ProfileFragment()).commit();
            }
        }.start();
    }
}
