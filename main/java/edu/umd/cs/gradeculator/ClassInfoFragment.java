package edu.umd.cs.gradeculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.service.CourseService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Michael on 4/21/2017.
 */

public class ClassInfoFragment extends Fragment {
    private static final String ARG_COURSE_ID = "classId";
    private static final String COURSE_UPDATED = "CourseUpdated";
    private Course course;
    private EditText exam_edit;
    private EditText quiz_edit;
    private EditText assignment_edit;
    private EditText project_edit;
    private EditText extra_edit;
    private ToggleButton exam_btn;
    private ToggleButton quiz_btn;
    private ToggleButton assignment_btn;
    private ToggleButton project_btn;
    private ToggleButton extra_btn;
    private CourseService cs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String courseId = getArguments().getString(ARG_COURSE_ID);
        cs = DependencyFactory.getCourseService(getActivity());
        course = DependencyFactory.getCourseService(getActivity()).getCourseById(courseId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_info, container, false);

        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);


        exam_btn = (ToggleButton) view.findViewById(R.id.exam_btn);
        quiz_btn = (ToggleButton) view.findViewById(R.id.quiz_btn);
        assignment_btn = (ToggleButton) view.findViewById(R.id.assignment_btn);
        project_btn = (ToggleButton) view.findViewById(R.id.project_btn);
        extra_btn = (ToggleButton) view.findViewById(R.id.extra_btn);

        // default equal weight
        exam_btn.setChecked(course.getEqual_weight_exam());
        quiz_btn.setChecked(course.getEqual_weight_quiz());
        assignment_btn.setChecked(course.getEqual_weight_assignment());
        project_btn.setChecked(course.getEqual_weight_project());
        extra_btn.setChecked(course.getEqual_weight_extra());

        exam_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, equal weight
                    course.setEqual_weight_exam(true);
                    Log.d("hi","here");
                } else {
                    // The toggle is disabled
                    course.setEqual_weight_exam(false);
                }
            }
        });


        quiz_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, equal weight
                    course.setEqual_weight_quiz(true);
                } else {
                    // The toggle is disabled
                    course.setEqual_weight_quiz(false);
                }
            }
        });

        assignment_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, equal weight
                    course.setEqual_weight_assignment(true);
                } else {
                    // The toggle is disabled
                    course.setEqual_weight_assignment(false);
                }
            }
        });

        project_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, equal weight
                    course.setEqual_weight_project(true);
                } else {
                    // The toggle is disabled
                    course.setEqual_weight_project(false);
                }
            }
        });

        extra_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, equal weight
                    course.setEqual_weight_extra(true);
                } else {
                    // The toggle is disabled
                    course.setEqual_weight_extra(false);
                }
            }
        });

        exam_edit = (EditText) view.findViewById(R.id.exam_edit_text);
        exam_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(exam_edit.hasFocus()){
                    exam_edit.setCursorVisible(true);
                    exam_edit.setSelection(exam_edit.getText().length());
                }
            }
        });
        if(course.getExam_weight() > 0){
            exam_edit.setText("" + course.getExam_weight());
        }


        quiz_edit = (EditText) view.findViewById(R.id.quiz_edit_text);
        quiz_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(quiz_edit.hasFocus()){
                    quiz_edit.setCursorVisible(true);
                    quiz_edit.setSelection(quiz_edit.getText().length());
                }
            }
        });
        if(course.getQuiz_weight() > 0){
            quiz_edit.setText("" + course.getQuiz_weight());
        }

        assignment_edit = (EditText) view.findViewById(R.id.assignment_edit_text);
        assignment_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(assignment_edit.hasFocus()){
                    assignment_edit.setCursorVisible(true);
                    assignment_edit.setSelection(assignment_edit.getText().length());
                }
            }
        });
        if(course.getAssignment_weight() > 0){
            assignment_edit.setText("" + course.getAssignment_weight());
        }


        project_edit = (EditText) view.findViewById(R.id.project_edit_text);
        project_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(project_edit.hasFocus()){
                    project_edit.setCursorVisible(true);
                    project_edit.setSelection(project_edit.getText().length());
                }
            }
        });
        if(course.getProject_weight() > 0){
            project_edit.setText("" + course.getProject_weight());
        }

        extra_edit = (EditText) view.findViewById(R.id.extra_edit_text);
        extra_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(extra_edit.hasFocus()){
                    extra_edit.setCursorVisible(true);
                    extra_edit.setSelection(extra_edit.getText().length());
                }
            }
        });
        if(course.getExam_weight() > 0){
            extra_edit.setText("" + course.getExtra_weight());
        }

        toolbar.findViewById(R.id.toolbar_save_classinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Since we already limit the input to be decimal so we just need to check if the
                // user enter or not. IMPOSSIBLE TO IMPUT NON NUMBER OR NEGATIVE NUMBER
                if(course == null) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Opps..Something is wrong")
                            .setMessage("Make sure the course itself is create first")
                            .setPositiveButton(R.string.okay, null)
                            .show();
                }else{
                    // course is already established
                    // start extracting data
                    double exam_double = 0.0,quiz_double = 0.0,assignment_double = 0.0,
                            project_double = 0.0,extra_double = 0.0,total = 0.0;

                    // Extracting any non-empty data one by one
                    if(exam_edit.getText().toString().length() > 0 ){
                        exam_double = Double.parseDouble(exam_edit.getText().toString());
                    }
                    if(quiz_edit.getText().toString().length() > 0 ){
                        quiz_double = Double.parseDouble(quiz_edit.getText().toString());
                    }
                    if(assignment_edit.getText().toString().length() > 0 ){
                        assignment_double = Double.parseDouble(assignment_edit.getText().toString());
                    }
                    if(project_edit.getText().toString().length() > 0 ){
                        project_double = Double.parseDouble(project_edit.getText().toString());
                    }
                    if(extra_edit.getText().toString().length() > 0 ){
                        extra_double = Double.parseDouble(extra_edit.getText().toString());
                    }
                    total = exam_double + quiz_double + assignment_double + project_double;
                    if(Double.compare(total, 100.0) == 0){
                        course.setAssignments_weight(assignment_double);
                        course.setExam_weight(exam_double);
                        course.setQuiz_weight(quiz_double);
                        course.setExtra_weight(extra_double);
                        course.setProject_weight(project_double);

                        Intent data = new Intent();
                        data.putExtra(COURSE_UPDATED, course);
                        getActivity().setResult(RESULT_OK, data);
                        getActivity().finish();
                    } else{
                        // total exceeding 100
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Opps..")
                                .setMessage("Make sure the total weight sum up to 100%.")
                                .setPositiveButton(R.string.okay,null)
                                .show();
                    }
                }
            }
        });


        toolbar.findViewById(R.id.toolbar_cancel_classinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    public static ClassInfoFragment newInstance(String classId) {
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_ID, classId);

        ClassInfoFragment fragment = new ClassInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Course getCourse(Intent data) {
        return (Course)data.getSerializableExtra(COURSE_UPDATED);
    }


}
