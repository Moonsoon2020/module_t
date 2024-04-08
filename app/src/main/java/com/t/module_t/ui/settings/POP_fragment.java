package com.t.module_t.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.t.module_t.R;
import com.t.module_t.databinding.PopFragmentBinding;


public class POP_fragment extends Fragment {

    private PopFragmentBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PopFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView textView = root.findViewById(R.id.back4);
        textView.setOnClickListener(v -> getFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ProfileFragment()).commit());
        return root;
    }
}
