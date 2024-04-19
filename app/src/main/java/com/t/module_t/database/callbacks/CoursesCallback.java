package com.t.module_t.database.callbacks;


import com.t.module_t.database.entity.Course;

import java.util.ArrayList;

public interface CoursesCallback {
    void onArrayCourseFetch(ArrayList<Course> courses);

}
