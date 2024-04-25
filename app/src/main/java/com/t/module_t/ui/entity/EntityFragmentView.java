package com.t.module_t.ui.entity;

import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class EntityFragmentView {
    private Class<? extends Fragment> fragmentClass;
    private ImageView view;
    public EntityFragmentView(Class<? extends Fragment> courseFragmentClass, ImageButton view){
        this.view = view;
        fragmentClass = courseFragmentClass;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public void setView(ImageView view) {
        this.view = view;
    }

    public ImageView getView() {
        return view;
    }
}
