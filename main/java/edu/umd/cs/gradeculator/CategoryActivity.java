package edu.umd.cs.gradeculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.gradeculator.model.Work;

public class CategoryActivity extends SingleFragmentActivity {

    private static final String EXTRA_COURSE_ID = "COURSE_ID";
    private static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    private Fragment fragment;

    @Override
    protected Fragment createFragment() {

        String courseId = getIntent().getStringExtra(EXTRA_COURSE_ID);
        Work.Category category = (Work.Category) getIntent().getSerializableExtra(EXTRA_CATEGORY_NAME);

        fragment = CategoryFragment.newInstance( courseId,category);
        return fragment;
    }



    public static Intent newIntent(Context context, String courseId, Work.Category category) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        intent.putExtra(EXTRA_CATEGORY_NAME, category);

        return intent;
    }

    @Override
    public void onBackPressed() {
        ((CategoryFragment) fragment).onBackPressed();
        super.onBackPressed();
    }

}
