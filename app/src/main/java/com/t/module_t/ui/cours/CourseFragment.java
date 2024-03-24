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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t.module_t.ui.cours.new_course.NewCourse;
import com.t.module_t.R;
import com.t.module_t.database.DataBaseControl;
import com.t.module_t.database.User;
import com.t.module_t.databinding.FragmentCourseBinding;
import com.t.module_t.ui.cours.new_course.SettingsCourse;

import java.util.ArrayList;

public class CourseFragment extends Fragment {
    private final String TAG = "CourseFragment";
    private FragmentCourseBinding binding;
    private Spinner spinner;
    private DataBaseControl control;
    private ArrayList<String> items_id_course;

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
            control = new DataBaseControl();
            spinner = bar.getCustomView().findViewById(R.id.spinner);
            items_id_course = new ArrayList<>();

            ArrayList<String> items = new ArrayList<>();
            Spinner spinner = bar.getCustomView().findViewById(R.id.spinner);
            AdapterCourseBar adapter = new AdapterCourseBar(getContext(),
                    android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), user -> {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                for (String i : user.id_courses) {
                    mDatabase.child("course").child(i).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String string = snapshot.child("courseName").getValue().toString();
                            items.add(string);
                            items_id_course.add(snapshot.child("id_course").getValue().toString());
                            if (string.equals(user.like_course)) {
                                spinner.setSelection(items.size() - 1);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            control.getUser(FirebaseAuth.getInstance().getCurrentUser().getEmail(), user -> {
                if (((User) user).status) {
                    ImageButton imageButton_add = bar.getCustomView().findViewById(R.id.image_button_course_add);
                    ImageButton imageButton_set = bar.getCustomView().findViewById(R.id.imagebutton_course_settings);
                    try {
                        imageButton_set.setVisibility(View.VISIBLE);
                        imageButton_add.setVisibility(View.VISIBLE);
                        imageButton_set.setOnClickListener(v -> {
                            Intent intent = new Intent(requireActivity(), SettingsCourse.class);
                            intent.putExtra("id_course", items_id_course.get(spinner.getSelectedItemPosition()));
                            startActivity(intent);
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
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DataBaseControl control = new DataBaseControl();
                control.getCourse(items_id_course.get(position), v ->{
//                    for (:
//                         ) {
//
//                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d(TAG, "ppp");
            }

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (spinner.getSelectedItem() == null)
            return;
        if (spinner.getSelectedItem().toString().isEmpty())
            return;
        new DataBaseControl().set_like_item_course(FirebaseAuth.getInstance().getCurrentUser().getEmail(), spinner.getSelectedItem().toString());
    }
}
