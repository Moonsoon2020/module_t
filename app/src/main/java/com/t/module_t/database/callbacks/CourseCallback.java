package com.t.module_t.database.callbacks;

import com.t.module_t.database.entity.Course;

public interface CourseCallback {
    void onCourseFetch(Course course);
}
