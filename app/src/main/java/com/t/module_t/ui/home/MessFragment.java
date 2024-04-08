package com.t.module_t.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.t.module_t.databinding.MessFragmentBinding;


public class MessFragment extends Fragment {
    private final String TAG = "HomeFragment";


    private MessFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MessViewModel homeViewModel =
                new ViewModelProvider(this).get(MessViewModel.class);
// менеджер компоновки, который позволяет получать доступ к layout с наших ресурсов
        Log.i(TAG, "create");
        binding = MessFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}