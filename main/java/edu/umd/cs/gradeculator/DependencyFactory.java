package edu.umd.cs.gradeculator;

import android.content.Context;

import edu.umd.cs.gradeculator.service.CourseService;
import edu.umd.cs.gradeculator.service.impl.SQLiteCourseService;

/**
 * Created by howardksw1 on 4/5/17.
 */

public class DependencyFactory {
    private static CourseService courseService;

    public static CourseService getCourseService(Context context) {
//        if (courseService == null) {
//            courseService = new InMemoryCourseService(context);
//        }
        if (courseService == null) {
            courseService = new SQLiteCourseService(context);
        }
        return courseService;
    }
}
