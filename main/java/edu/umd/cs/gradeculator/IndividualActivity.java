package edu.umd.cs.gradeculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.gradeculator.model.Work;

public class IndividualActivity extends SingleFragmentActivity {


    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_WORK_NAME = "WORK_NAME";
    private static final String EXTRA_CATEGORY = "CATEGORY";
    private static final String EXTRA_COURSE_ID = "COURSE_ID";

    @Override
    protected Fragment createFragment() {
        String workName = getIntent().getStringExtra(EXTRA_WORK_NAME);
        String category = getIntent().getStringExtra(EXTRA_CATEGORY);
        String courseId = getIntent().getStringExtra(EXTRA_COURSE_ID);

        return IndividualFragment.newInstance(workName,category,courseId);
    }

    public static Intent newIntent(Context context, String workName,String category,String courseId ) {  // change storyId to workId?
        Intent intent = new Intent(context, IndividualActivity.class);
        intent.putExtra(EXTRA_WORK_NAME, workName);
        intent.putExtra(EXTRA_CATEGORY, category);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        return intent;
    }

    public static Work getStoryCreated(Intent data) {
        return IndividualFragment.getworkCreated(data);
    }



}
