package edu.umd.cs.gradeculator.service.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;

/**
 * Created by howardksw1 on 4/5/17.
 */

public class InMemoryCourseService  {
    private Context context;
    private ArrayList<Course> courses;

    public InMemoryCourseService(Context context) {
        this.context = context;
        this.courses = new ArrayList<Course>();
    }

//    @Override
    public void addCourseToBacklog(Course course) {
        Course currCourse = getCourseById(course.getId());
        if (currCourse == null) {
            courses.add(course);
        } else {
            currCourse.setTitle(course.getTitle());
            currCourse.setIdentifier(course.getIdentifier());
            //currCourse.setDesire_grade(course.getDesire_grade());
            currCourse.setCurrent_grade(course.getCurrent_grade());
        }
    }

//    @Override
    public Course getCourseById(String id) {
        for(Course course: courses) {
            if(course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }

//    @Override
    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> prioritizedStories = new ArrayList<Course>(courses);
        Collections.sort(prioritizedStories, new Comparator<Course>() {
            @Override
            public int compare(Course course1, Course course2) {
                return course1.getIdentifier().compareTo(course2.getIdentifier());
            }
        });

        return prioritizedStories;
    }

//    @Override
    public boolean remove_course(int position) {
        courses = getAllCourses();
        Course to_delete = courses.get(position);

        if (to_delete != null) {
            courses.remove(to_delete);
            courses.trimToSize();
            return true;
        }
        return false;
    }
}
