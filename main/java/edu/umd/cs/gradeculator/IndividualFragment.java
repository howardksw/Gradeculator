package edu.umd.cs.gradeculator;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;
import edu.umd.cs.gradeculator.service.impl.AlarmRecever;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static edu.umd.cs.gradeculator.model.Work.Category.EXAM;

public class IndividualFragment extends Fragment {
    private static final String EXTRA_WORK_CREATED = "WorkCreated";
    private static final String ARG_WORK_TITLE = "WorkTitle";
    private static final String ARG_COURSE_ID= "CourseId";
    private static final String ARG_CAT="Cat";
    private Work work;
    private CourseService ss;
    private String cat;
    private String title;
    private EditText gradeNameEditText;
    private EditText totalPointsEditText;
    private EditText PointsEditText;
    private EditText weightEditText;
    private String cId;
    private ArrayList<Work> works;
    private TableLayout assignNameLayout;
    private TableLayout dueLayout;
    private TableLayout totalPointLayout;
    private TableLayout gradeLayout;
    private TableLayout weightLayout;
    private ToggleButton complete_btn;
    private LinearLayout layout;
    private View complete_line;
    private View weight_line;
    private boolean equal_weight, finished;
    private double oldwork=0;

    private TextView due_date;
    private String date;
    private DatePickerDialog datePicker;
    private SimpleDateFormat sdf;

    public static IndividualFragment newInstance(String workTitle,String category,String courseId) {
        Bundle args = new Bundle();
        args.putString(ARG_WORK_TITLE, workTitle);
        args.putString(ARG_COURSE_ID,courseId);
        args.putString(ARG_CAT,category);
        IndividualFragment fragment = new IndividualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cId = getArguments().getString(ARG_COURSE_ID);
        cat = getArguments().getString(ARG_CAT);
        title = getArguments().getString(ARG_WORK_TITLE);
        equal_weight = true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        layout = (LinearLayout) view.findViewById(R.id.individual_page);

        String myFormat = "MM/dd/yyyy"; //the format for the date
        sdf = new SimpleDateFormat(myFormat); // formatter
        TextView cateTitle=(TextView) view.findViewById(R.id.toolbar_title_individual);
        cateTitle.setText(cat);
        //gets the course
        CourseService ss=DependencyFactory.getCourseService(getActivity());
        Course cs=ss.getCourseById(cId);

        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        assignNameLayout = (TableLayout)view.findViewById(R.id.assignNameLayout);
        dueLayout = (TableLayout)view.findViewById(R.id.dueLayout);
        totalPointLayout = (TableLayout)view.findViewById(R.id.total_point_layout);
        gradeLayout = (TableLayout)view.findViewById(R.id.gradeLayout);
        weightLayout = (TableLayout)view.findViewById(R.id.weightLayout);
        complete_btn = (ToggleButton) view.findViewById(R.id.complete_btn);
        complete_line = view.findViewById(R.id.complete_line);
        weight_line = view.findViewById(R.id.weight_line);

        // default not completed
        complete_btn.setChecked(false);
        gradeLayout.setVisibility(View.GONE);

        complete_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, enter weight
                    gradeLayout.setVisibility(View.VISIBLE);
                    work.setCompleteness(true); //null
                } else {
                    // The toggle is disabled
                    gradeLayout.setVisibility(View.GONE);
                    work.setCompleteness(false);
                }
            }
        });

        due_date = (TextView) view.findViewById(R.id.dueDate);
        final Calendar c = Calendar.getInstance();

        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        if (title!=null){
            switch(cat){
                case "Exam":
                    works=cs.getExams();
                    break;
                case "Quiz":
                    works=cs.getQuzs();
                    break;
                case "Assignment":
                    works=cs.getAssigs();
                    break;
                case "Project":
                    works=cs.getProjs();
                    break;
                case "Extra":
                    works=cs.getExtra();
                    break;
                default:
                    works=new ArrayList<Work>();
            }
            for(Work cWrok:works){
                if(cWrok.getTitle().equals(title)){
                    work=cWrok;
                    oldwork=work.getWeight();

                }
            }
        }

        gradeNameEditText = (EditText) view.findViewById(R.id.igradeName);
        if(work != null){
            gradeNameEditText.setText(work.getTitle());
        }
        // these array adapters are causing  IndexOutOfBoundsException: Invalid index 116, size is 2

        totalPointsEditText = (EditText) view.findViewById(R.id.itotalPoints);
        if(work != null){
            totalPointsEditText.setText(""+work.getPossible_points());
        }

        PointsEditText = (EditText) view.findViewById(R.id.ipoint);
        if(work != null){
            PointsEditText.setText(""+work.getEarned_points());
        }
        weightEditText = (EditText) view.findViewById(R.id.iweight);
        if(work != null){
            weightEditText.setText(""+work.getWeight());
        }
        if(work !=null){
            //initialize the due date textview if there's one
            due_date.setText(sdf.format(work.getDueDate().getTime()).trim());
            due_date.setTextColor(getResources().getColor(R.color.text_color));
        }

        //Check whether need to display weight layout or not
        if (cat!=null) {
            switch (cat) {
                case "Exam":
                    if (cs.getEqual_weight_exam()) {
                        weightLayout.setVisibility(View.GONE);
                        weight_line.setVisibility(View.GONE);
                        equal_weight = true ;
                    }
                    else{
                        weightLayout.setVisibility(View.VISIBLE);
                        weight_line.setVisibility(View.VISIBLE);
                        equal_weight = false;
                    }
                    break;
                case "Quiz":
                    if (cs.getEqual_weight_quiz()) {
                        weightLayout.setVisibility(View.GONE);
                        weight_line.setVisibility(View.GONE);
                        equal_weight = true;
                    }
                    else{
                        weightLayout.setVisibility(View.VISIBLE);
                        weight_line.setVisibility(View.VISIBLE);
                        equal_weight = false;
                    }
                    break;
                case "Assignment":
                    if (cs.getEqual_weight_assignment()) {
                        weightLayout.setVisibility(View.GONE);
                        weight_line.setVisibility(View.GONE);
                        equal_weight = true;
                    }
                    else{
                        weightLayout.setVisibility(View.VISIBLE);
                        weight_line.setVisibility(View.VISIBLE);
                        equal_weight =false;
                    }
                    break;
                case "Project":
                    if (cs.getEqual_weight_project()) {
                        weightLayout.setVisibility(View.GONE);
                        weight_line.setVisibility(View.GONE);
                        equal_weight =true;
                    }
                    else{
                        weightLayout.setVisibility(View.VISIBLE);
                        weight_line.setVisibility(View.VISIBLE);
                        equal_weight =false;
                    }
                    break;
                case "Extra":
                    if (cs.getEqual_weight_extra()) {
                        weightLayout.setVisibility(View.GONE);
                        weight_line.setVisibility(View.GONE);
                        equal_weight =true;
                    }
                    else{
                        weightLayout.setVisibility(View.VISIBLE);
                        weight_line.setVisibility(View.VISIBLE);
                        equal_weight =false;
                    }
                    break;
                default:

            }
        }

        if (work == null) {
            work = new Work(EXTRA_WORK_CREATED);
        }

        if(work!=null) {
            if (work.getCompleteness()) {
                complete_btn.setChecked(true);
                gradeLayout.setVisibility(View.VISIBLE);
            } else {
                complete_btn.setChecked(false);
                gradeLayout.setVisibility(View.GONE);
            }
        } else {
            complete_btn.setChecked(false);
            gradeLayout.setVisibility(View.GONE);
        }

        toolbar.findViewById(R.id.toolbar_cancel_individual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        toolbar.findViewById(R.id.toolbar_save_individual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputsAreValid()) {

                    work.setTitle(gradeNameEditText.getText().toString().trim().replaceAll(" +", " "));

                    try {
                        if(date != null) {
                            //the date will only change if the user select a different date

                            work.setDueDate(sdf.parse(date));
                        }
                    }catch(ParseException e){
                        //format error
                    }
                    work.setPossible_points(Math.round(Double.parseDouble(
                            totalPointsEditText.getText().toString()) * 10.0) / 10.0);

                    if(work.getCompleteness()) {
                        //only set earned points when the assignment is finished
                        work.setEarned_points(Math.round(Double.parseDouble(
                                PointsEditText.getText().toString()) * 10.0) / 10.0);
                    } else {
                        work.setEarned_points(0.0);
                    }

                    if(!equal_weight) {
                        // only set weight if the weight is not equally
                        double weight = Double.parseDouble(weightEditText.getText().toString());

                        work.setWeight(weight);
                    }else {
                        work.setWeight(-1);
                    }


                    Intent data = new Intent();
                    switch(cat){
                        case "Exam":
                            work.setCategory(EXAM);
                            break;
                        case "Quiz":
                            work.setCategory(Work.Category.QUIZ);
                            break;
                        case "Assignment":
                            work.setCategory(Work.Category.ASSIGNMENT);
                            break;
                        case "Project":
                            work.setCategory(Work.Category.PROJECT);
                            break;
                        case "Extra":
                            work.setCategory(Work.Category.EXTRA);
                            break;

                    }
                    CourseService sss = DependencyFactory.getCourseService(getActivity());
                    Course cs =sss.getCourseById(cId);
                    if(title==null){
                        sss.addCourseToBacklog(cs,work, title);
                    }else{
                        sss.addCourseToBacklog(cs,work, title);
                    }

                    cancelManyNotification(work.getDueDate(),work.getCategory(),work.getTitle(),cId);
                    scheduleManyNotification(work.getDueDate(),work.getCategory(),work.getTitle(),cId);

                    data.putExtra(EXTRA_WORK_CREATED, work);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                } else{

                    // invalid input, shake each invalid edit text
                    if(gradeNameEditText.getText().toString().trim().length()<=0){
                        assignNameLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        gradeNameEditText.startAnimation(shake);
                    } else{
                        assignNameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    if(due_date.getCurrentTextColor() == getResources().getColor(R.color.hint_color)){
                        dueLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        due_date.startAnimation(shake);
                    } else{
                        dueLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    if(totalPointsEditText.getText().toString().trim().length()<=0){
                        totalPointLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        totalPointsEditText.startAnimation(shake);
                    } else{
                        totalPointLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    if(work.getCompleteness()) {
                        if (PointsEditText.getText().toString().trim().length()<=0 ||
                                Double.parseDouble(PointsEditText.getText().toString().trim()) >
                                Double.parseDouble(totalPointsEditText.getText().toString().trim())) {
                            gradeLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                            PointsEditText.startAnimation(shake);
                        } else {
                            gradeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        }
                    }
                    CourseService ss=DependencyFactory.getCourseService(getActivity());
                    Course cs=ss.getCourseById(cId);

                    if(weightEditText.getText().toString().trim().length()<=0){
                        weightLayout.setBackgroundColor(getResources().getColor(R.color.alter_color));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_edit_text);
                        weightEditText.startAnimation(shake);
                    } else{
                        if(!cs.ckeckWeight(cat,Double.parseDouble(weightEditText.getText().toString())-oldwork)){
                            // total weight exceed maximum
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Opps..")
                                    .setMessage("Your total weight for this category exceeds the maximum possible " +
                                            "weight that you entered.")
                                    .setPositiveButton(R.string.okay,null)
                                    .show();
                        }
                        weightLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }
                }
            }
        });


        return view;
    }

    public static Work getworkCreated(Intent data) {
        return (Work)data.getSerializableExtra(EXTRA_WORK_CREATED);
    }

    private boolean inputsAreValid() {
        CourseService ss=DependencyFactory.getCourseService(getActivity());
        Course cs=ss.getCourseById(cId);
       if(equal_weight && work.getCompleteness()) {
            //the assignment is finished and equally weighted
            return
                    gradeNameEditText.getText().toString().trim().length() > 0 &&
                            totalPointsEditText.getText().toString().length() > 0 &&
                            PointsEditText.getText().toString().length() > 0 &&
                            Double.parseDouble(PointsEditText.getText().toString().trim()) <=
                                    Double.parseDouble(totalPointsEditText.getText().toString().trim()) &&
//                            PointsEditText.getText().toString().length() <=
//                                    totalPointsEditText.getText().toString().length() &&
                            due_date.getText().toString().length() > 0;

        }
        else if (!equal_weight && work.getCompleteness()){
           //the assignment is finished and NOT equally weighted
           return
                   gradeNameEditText.getText().toString().trim().length() > 0 &&
                           totalPointsEditText.getText().toString().length() > 0 &&
                           PointsEditText.getText().toString().length() > 0 &&
                           Double.parseDouble(PointsEditText.getText().toString().trim()) <=
                                   Double.parseDouble(totalPointsEditText.getText().toString().trim()) &&
//                           PointsEditText.getText().toString().length() <=
//                                   totalPointsEditText.getText().toString().length() &&
                           weightEditText.getText().toString().length() > 0 &&
                           due_date.getText().toString().length() > 0&&cs.ckeckWeight(cat,Double.parseDouble(weightEditText.getText().toString())-oldwork);
       }
       else if (!equal_weight && !work.getCompleteness()){
           //the assignment is not finished and not equally weighted

           return
                   gradeNameEditText.getText().toString().trim().length() > 0 &&
                           totalPointsEditText.getText().toString().length() > 0 &&
                           weightEditText.getText().toString().length() > 0 &&
                           due_date.getText().toString().length() > 0 && cs.ckeckWeight(cat,Double.parseDouble(weightEditText.getText().toString())-oldwork);
       }
       else{
           //the assignment is not finished or equally weighted
           return
                   gradeNameEditText.getText().toString().trim().length() > 0 &&
                           totalPointsEditText.getText().toString().length() > 0 &&
                                   due_date.getText().toString().length() > 0;
       }
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date");
    }

    public void scheduleManyNotification(Date date,Work.Category category,String title,String courseId){
        if(title != null) {
            switch (category) {
                case EXAM:
                    for (int x = 0; x < 5; x++) {
                        //id to be determined haha
                        scheduleNotification(date, title.hashCode() + x, title, 4 - x,courseId,category);
                    }
                    break;
                case ASSIGNMENT:
                    for (int x = 0; x < 3; x++) {
                        //id to be determined haha
                        scheduleNotification(date, title.hashCode() + x, title, 2 - x,courseId,category);
                    }
                    break;
                case PROJECT:
                    for (int x = 0; x < 8; x++) {
                        //id to be determined haha
                        scheduleNotification(date, title.hashCode() + x, title,  7 - x,courseId,category);
                    }
                    break;
                case QUIZ:
                    for (int x = 0; x < 2; x++) {
                        //id to be determined haha
                        scheduleNotification(date, title.hashCode() + x, title, 1 - x,courseId,category);
                    }
                    break;
                default:
                    break;

            }
        }
    }


    public void cancelManyNotification(Date date,Work.Category category,String title,String courseId){
        if(title != null) {
            switch (category) {
                case EXAM:
                    for (int x = 0; x < 5; x++) {
                        //id to be determined haha
                        cancelNotification(title.hashCode() + x, title, 4 - x,courseId,category);
                    }
                    break;
                case ASSIGNMENT:
                    for (int x = 0; x < 3; x++) {
                        //id to be determined haha
                        cancelNotification(title.hashCode() + x, title, 2 - x,courseId,category);
                    }
                    break;
                case PROJECT:
                    for (int x = 0; x < 8; x++) {
                        //id to be determined haha
                        cancelNotification(title.hashCode() + x, title,  7 - x,courseId,category);
                    }
                    break;
                case QUIZ:
                    for (int x = 0; x < 2; x++) {
                        //id to be determined haha
                        cancelNotification(title.hashCode() + x, title, 1 - x,courseId,category);
                    }
                    break;
                default:
                    break;

            }
        }
    }





    public void scheduleNotification(Date date, int id, String title, int days, String courseId, Work.Category category){
        Log.d(title + " " + date.toString() + " " + Integer.toString(days), Integer.toString(id));
        long millis = date.getTime();
        long current = System.currentTimeMillis();
        long interval = millis - current;

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        long intervalToAlarm = interval - days * oneDayInMillis;


        AlarmManager alarmManager = (AlarmManager)  getActivity().getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), AlarmRecever.class);
        myIntent.putExtra("title",title);
        myIntent.putExtra("days",days);
        myIntent.putExtra("category",category);
        myIntent.putExtra("courseId",courseId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + intervalToAlarm, pendingIntent);
    }

    public void cancelNotification(int id,String title,int days,String courseId, Work.Category category){
        AlarmManager alarmManager = (AlarmManager)  getActivity().getApplicationContext().getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity().getApplicationContext(), AlarmRecever.class);
        myIntent.putExtra("title",title);
        myIntent.putExtra("days",days);
        myIntent.putExtra("category",category);
        myIntent.putExtra("courseId",courseId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),
                id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }


    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            if(work != null && work.getDueDate() != null){
                Calendar cal = Calendar.getInstance();
                cal.setTime(work.getDueDate());
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialog and return it
            datePicker = new DatePickerDialog(getActivity(), R.style.datepicker, this, year, month, day);
            return datePicker;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            date = month + 1 + "/" + day + "/" + year;
            String m = Integer.toString(month+1);
            String d = Integer.toString(day);
            due_date.setTextColor(getResources().getColor(R.color.text_color));
            if(month < 9){
                m = "0" + Integer.toString(month+1);
            }
            if(day < 10){
                d = "0" + Integer.toString(day);
            }

            due_date.setText(m + "/" + d + "/" + year);
        }
    }
}
