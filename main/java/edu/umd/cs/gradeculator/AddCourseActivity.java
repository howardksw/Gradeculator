package edu.umd.cs.gradeculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.gradeculator.model.Course;

/**
 * Created by kay on 4/20/17.
 */

public class AddCourseActivity extends SingleFragmentActivity {

    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_COURSE_ID = "COURSE_ID";

    @Override
    protected Fragment createFragment() {
        String courseId = getIntent().getStringExtra(EXTRA_COURSE_ID);

        return AddCourseFragment.newInstance(courseId);
    }

    public static Intent newIntent(Context context, String courseId) {
        Intent intent = new Intent(context, AddCourseActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        return intent;
    }

    public static Course getCourseCreated(Intent data) {
        return AddCourseFragment.getCourseCreated(data);
    }


}

