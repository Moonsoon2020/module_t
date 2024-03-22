package com.t.module_t.ui.cours;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.t.module_t.database.Course;
import com.t.module_t.new_course.NewCourse;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.databinding.FragmentCourseBinding;

import java.util.ArrayList;

public class CourseFragment extends Fragment {
    private final String TAG = "CourseFragment";
    private FragmentCourseBinding binding;
    private Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CourseViewModel courseViewModel =
                new ViewModelProvider(this).get(CourseViewModel.class);

        binding = FragmentCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.i(TAG, "create");
        ActionBar bar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (bar != null) {
            bar.setCustomView(R.layout.course_appbar);
            bar.setDisplayShowCustomEnabled(true);
            DataBaseControl control = new DataBaseControl();
            spinner = bar.getCustomView().findViewById(R.id.spinner);
            ArrayList<String> item = new ArrayList<>();
            item.add("");
            Spinner spinner = bar.getCustomView().findViewById(R.id.spinner);
            AdapterCourseBar adapter = new AdapterCourseBar(getContext(),
                    android.R.layout.simple_spinner_item, item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), user -> {
                control.getCoursesOnUser(user, courses -> {
                    for (Course i : courses) {
                        item.add(i.getCourseName());
                    }
                    item.remove("");
                    spinner.setSelection(adapter.getPosition(user.like_course)); // не ломает

                });
            });
            control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), user -> {
                if (((User) user).status) {
                    ImageButton imageButton_add = bar.getCustomView().findViewById(R.id.image_button_course_add);
                    ImageButton imageButton_set = bar.getCustomView().findViewById(R.id.imagebutton_course_settings);
                    try {
                        imageButton_set.setVisibility(View.VISIBLE);
                        imageButton_add.setVisibility(View.VISIBLE);
                        imageButton_set.setOnClickListener(v -> {

                        });
                        imageButton_add.setOnClickListener(v -> {
                            Intent intent = new Intent(requireActivity(), NewCourse.class);
                            startActivity(intent);
                        });
                    } catch (NullPointerException e) {
                        Log.e(TAG, "Exception: " + e);
                    }

                }
            });
        }
        RecyclerView recyclerView = root.findViewById(R.id.recycler_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        ArrayList<CourseElement> courses = new ArrayList<>();
        courses.add(new CourseElement("ghbdtn"));
        CourseAdapter courseAdapter = new CourseAdapter(requireActivity(), courses);
        recyclerView.setAdapter(courseAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        new DataBaseControl().set_like_item_course(FirebaseAuth.getInstance().getCurrentUser().getEmail(), spinner.getSelectedItem().toString());
    }
}
