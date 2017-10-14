package edu.umd.cs.gradeculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;

import edu.umd.cs.gradeculator.model.Course;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
/**
 * Created by kay on 4/20/17.
 */

public class AddCourseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_COURSE_CREATED = "CourseCreated";
    private static final String ARG_COURSE_ID = "courseId";
    private static final int REQUEST_CODE_SAVE_STORY = 1;

    private Course course;

    private TableLayout nameLayout;
    private TableLayout titleLayout;
    private TableLayout creditLayout;
    private EditText courseNameEditText;
    private EditText courseTitleEditText;
    private EditText creditEditText;
    private Spinner grade_spinner;

    public static AddCourseFragment newInstance(String courseID){
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, courseID);

        AddCourseFragment fragment = new AddCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        String courseId = getArguments().getString(ARG_COURSE_ID);
        course = DependencyFactory.getCourseService(getActivity().getApplicationContext()).
                getCourseById(courseId);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcourse, container, false);

        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        nameLayout = (TableLayout)view.findViewById(R.id.nameLayout);
        titleLayout = (TableLayout)view.findViewById(R.id.titleLayout);
        creditLayout = (TableLayout)view.findViewById(R.id.creditLayout);

        courseNameEditText = (EditText)view.findViewById(R.id.course_name);
        courseNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(courseNameEditText.hasFocus()){
                    courseNameEditText.setCursorVisible(true);
                    courseNameEditText.setSelection(courseNameEditText.getText().length());
                }
            }
        });

        if(course != null){
            courseNameEditText.setText(course.getTitle());
        }
        courseTitleEditText = (EditText)view.findViewById(R.id.course_title);
        courseTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(courseTitleEditText.hasFocus()){
                    courseTitleEditText.setCursorVisible(true);
                    courseTitleEditText.setSelection(courseTitleEditText.getText().length());
                }
            }
        });


        if(course != null){
            courseTitleEditText.setText(course.getIdentifier());
        }

        creditEditText = (EditText)view.findViewById(R.id.credit);
        creditEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(creditEditText.hasFocus()){
                    creditEditText.setCursorVisible(true);
                    creditEditText.setSelection(creditEditText.getText().length());
                }
            }
        });
        if(course != null){
            creditEditText.setText("" + course.getCredit());
        }

        grade_spinner = (Spinner)view.findViewById(R.id.grade_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.grade_array, R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        grade_spinner.setAdapter(statusAdapter);
        if (course != null) {
            grade_spinner.setSelection(course.getGradePosition());
        } else {
            // set deafult
            grade_spinner.setSelection(statusAdapter.getPosition("A"));
        }

        toolbar.findViewById(R.id.toolbar_save_addcourse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputsAreValid()) {
                    if (course == null) {
                        course = new Course();
                    }


                    course.setTitle(courseNameEditText.getText().toString().trim().replaceAll(" +", " "));
                    course.setIdentifier(courseTitleEditText.getText().toString().trim().replaceAll(" +", " "));
                    course.setCredit(Integer.parseInt(creditEditText.getText().toString().trim().replaceAll(" +", " ")));
                    course.setGrade(grade_spinner.getSelectedItemPosition());
                    course.setDesire_grade(course.getGrade());

                    Intent data = new Intent();
                    data.putExtra(EXTRA_COURSE_CREATED, course);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                } else{
                    // invalid input, shake each invalid edit text
                    if(courseNameEditText.getText().toString().trim().length()<=0){
                        nameLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        courseNameEditText.startAnimation(shake);
                    } else{
                        nameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }


                    if(courseTitleEditText.getText().toString().trim().length()<=0){
                        titleLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        courseTitleEditText.startAnimation(shake);
                    }else{
                        titleLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }


                    if(creditEditText.getText().toString().trim().length()<=0){
                        creditLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        creditEditText.startAnimation(shake);
                    }else{
                        creditLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }
                }
            }
        });


        toolbar.findViewById(R.id.toolbar_cancel_addcourse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });


        return view;

    }

    public static Course getCourseCreated(Intent data) {
        return (Course)data.getSerializableExtra(EXTRA_COURSE_CREATED);
    }

    private boolean inputsAreValid() {
        return
                courseNameEditText.getText().toString().trim().length() > 0 &&
                        courseTitleEditText.getText().toString().trim().length() > 0 &&
                        creditEditText.getText().toString().trim().length() > 0 &&
                        grade_spinner.getSelectedItemPosition() >= 0;
    }
}
