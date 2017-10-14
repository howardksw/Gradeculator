package edu.umd.cs.gradeculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import edu.umd.cs.gradeculator.model.Course;

/**
 * Created by Michael on 4/21/2017.
 */

public class ClassInfoActivity extends SingleFragmentActivity {
    private final String TAG = getClass().getSimpleName();

    private static final String COURSE_ID = "COURSE_ID";

    @Override
    protected Fragment createFragment() {
        String storyId = getIntent().getStringExtra(COURSE_ID);
        return ClassInfoFragment.newInstance(storyId);
    }

    public static Intent newIntent(Context context, String classId) {
        Intent intent = new Intent(context, ClassInfoActivity.class);
        intent.putExtra(COURSE_ID, classId);
        return intent;
    }

    public static Course getCourse(Intent data) {
        return ClassInfoFragment.getCourse(data);
    }


}
