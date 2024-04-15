package com.t.module_t.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.t.module_t.R;
import com.t.module_t.databinding.DowlandAcBinding;


public class DownlandFragment extends Fragment {
    private DowlandAcBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Dowland", "Dowland");
        binding = DowlandAcBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Animation rotate = AnimationUtils.loadAnimation(this.getContext(), R.anim.dowland);
        root.findViewById(R.id.imageView_dow).startAnimation(rotate);
        return root;
    }

    public interface FragmentCreationListener {
        void onFragmentCreated(Fragment fragment);
    }

    public void getNewFragment(Context context, String name, FragmentCreationListener listener) {
        long startTime = System.currentTimeMillis();
        Fragment fragment = Fragment.instantiate(context, name);
        if (System.currentTimeMillis() - startTime < 5000) {
            listener.onFragmentCreated(fragment);
        } else {
            listener.onFragmentCreated(Fragment.instantiate(context, NoInternetFragment.class.getName()));
        }
    }
}
