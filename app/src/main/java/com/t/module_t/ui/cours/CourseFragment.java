package com.t.module_t.ui.cours;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t.module_t.R;
import com.t.module_t.ui.cours.CourseViewModel;
import com.t.module_t.databinding.FragmentCourseBinding;

import java.util.ArrayList;
import java.util.Objects;

public class CourseFragment extends Fragment {
    private final String TAG = "CourseFragment";
    private FragmentCourseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CourseViewModel courseViewModel =
                new ViewModelProvider(this).get(CourseViewModel.class);

        binding = FragmentCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.i(TAG, "create");
        // теперь можем получить наши элементы, расположенные во фрагменте
        RecyclerView recyclerView = root.findViewById(R.id.recycler_course); // тут проверь это меняли
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        ArrayList<CourseElement> courses = new ArrayList<>();
        courses.add(new CourseElement("ghbdtn"));
        CourseAdapter courseAdapter = new CourseAdapter(getActivity(), courses);
        recyclerView.setAdapter(courseAdapter);
//        final TextView textView = binding.textCourse;
//        CourseViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}