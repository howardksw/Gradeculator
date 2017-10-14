package edu.umd.cs.gradeculator.service;

import java.util.ArrayList;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;

/**
 * Created by howardksw1 on 4/5/17.
 */

public interface CourseService {
    public void addCourseToBacklog(Course course, Work work, String title);
    public Course getCourseById(String id);
    public ArrayList<Course> getAllCourses();
    public boolean remove_course(int position);
    public boolean removeWorkFromACourse(int position, Course course, Work.Category category);
}
