package edu.umd.cs.gradeculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.gradeculator.model.Course;

/**
 * Created by Ping on 4/24/17.
 */

public class CourseDirActivity extends SingleFragmentActivity{

    private final String TAG = getClass().getSimpleName();
    private static final String COURSE_ID = "COURSE_ID";
    private Fragment fragment;

    @Override
    protected Fragment createFragment() {
        String courseId = getIntent().getStringExtra(COURSE_ID);
        fragment = CourseDirFragment.newInstance(courseId);
        return fragment;
    }

    public static Intent newIntent(Context context, String classId) {
        Intent intent = new Intent(context, CourseDirActivity.class);
        intent.putExtra(COURSE_ID, classId);
        return intent;
    }

    public static Course getCourse(Intent data) {
        return CourseDirFragment.getCourse(data);
    }

    @Override
    public void onBackPressed() {
        ((CourseDirFragment) fragment).onBackPressed();
        super.onBackPressed();
    }
}
