package edu.umd.cs.gradeculator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;
import edu.umd.cs.gradeculator.service.CourseService;
import edu.umd.cs.gradeculator.service.ItemTouchHelperAdapter;

import static android.app.Activity.RESULT_OK;
import static edu.umd.cs.gradeculator.model.Work.Category;

//    Credits
//    This application uses Open Source components. You can find the source code of their open
//     source projects along with license information below. We acknowledge and are grateful to
//     these developers for their contributions to open source.
//
//    Project: Android-ItemTouchHelper-Demo https://github.com/iPaulPro/Android-ItemTouchHelper-Demo
//    Copyright 2015 Paul Burke
//    License (Apache 2.0) https://github.com/iPaulPro/Android-ItemTouchHelper-Demo/blob/master/LICENSE.txt
//    SimpleItemTouchHelperCallback and WorkAdapter are built based the above project.
//


/**
 * Created by weng2 on 4/12/2017.
 */

public class CategoryFragment extends Fragment {

    private RecycleViewWtEmpty categoryRecyclerView;
    private TextView categoryTitleTextView;
    private ItemTouchHelper mItemTouchHelper;

    private CourseService courseService;

    private WorkAdapter adapter;
    private static final String ARG_COURSE_ID = "courseId";
    private static final String ARG_CATEGORY_NAME = "categoryName";
    private static final int REQUEST_CODE_CREATE_WORK = 0;

    private String courseId;
    private Category category;
    private String categoryName;
    private List<Work> works;
    private Course course;
    @Nullable private View emptyView;


    public static CategoryFragment newInstance(String courseId, Category category) {
        Bundle args = new Bundle();

        args.putString(ARG_COURSE_ID, courseId);
        args.putSerializable(ARG_CATEGORY_NAME, category);

        CategoryFragment fragment = new CategoryFragment();

        fragment.setArguments(args);
        return fragment; //ss
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseService = DependencyFactory.getCourseService(getActivity().getApplicationContext());
        courseId = getArguments().getString(ARG_COURSE_ID);
        category = (Category) getArguments().getSerializable(ARG_CATEGORY_NAME);
        course = courseService.getCourseById(courseId);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_WORK) {
            if (data == null) {
                return;
            }
            //haha

//            Work workCreated = IndividualActivity.getStoryCreated(data);
//            courseService.getCourseById(courseId).add(workCreated);
//            courseService.addCourseToBacklog();
            updateUI();
        }
    }


    private void updateUI() {

        List<Work> works = new ArrayList<>();
//        Course theCourse = courseService.getCourseById(courseId);
        course = courseService.getCourseById(courseId);


        if (category != null) {
            if (category == Category.EXAM) {
                works = course.getExams();
                categoryName = "Exam";
            } else if (category == Category.QUIZ) {
                works = course.getQuzs();
                categoryName = "Quiz";
            } else if (category == Category.PROJECT) {
                works = course.getProjs();
                categoryName = "Project";
            } else if (category == Category.ASSIGNMENT) {
                works = course.getAssigs();
                categoryName = "Assignment";
            } else if (category == Category.EXTRA) {
                works = course.getExtra();
                categoryName = "Extra";
            } else {
                works = null;
                categoryName = "";
            }
        }

        if (works != null) {
            if (adapter == null) {
                adapter = new WorkAdapter(works);
                categoryRecyclerView.setAdapter(adapter);
                ItemTouchHelper.Callback callback = new CategoryFragment.SimpleItemTouchHelperCallback(adapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(categoryRecyclerView);
            } else {
                adapter.setWorks(works);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // make it take up the whole space
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        categoryTitleTextView = (TextView) view.findViewById(R.id.category_title);

        categoryTitleTextView.setText(categoryName);

        emptyView = (LinearLayout) view.findViewById(R.id.empty_view_category);
        categoryRecyclerView = (RecycleViewWtEmpty) view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecyclerView.setEmptyView(emptyView);

        toolbar.findViewById(R.id.toolbar_back_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }
        });

        toolbar.findViewById(R.id.toolbar_add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(IndividualActivity.newIntent(getActivity(), null, categoryName, courseId), REQUEST_CODE_CREATE_WORK);
            }
        });

        TextView empty_btn = (TextView) view.findViewById(R.id.empty_btn_category);
        empty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(IndividualActivity.newIntent(getActivity(), null, categoryName, courseId), REQUEST_CODE_CREATE_WORK);
            }
        });

        TextView center_title = (TextView) toolbar.findViewById(R.id.category_title);
        if (category != null) {
            if (category == Category.EXAM) {
                center_title.setText("Exams");
            } else if (category == Category.QUIZ) {
                center_title.setText("Quizzes");
            } else if (category == Category.PROJECT) {
                center_title.setText("Projects");
            } else if (category == Category.ASSIGNMENT) {
                center_title.setText("Assignments");
            } else if (category == Category.EXTRA) {
                center_title.setText("Extra Credits");
            }
        }
        updateUI();

        return view;
    }

    private class WorkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView dueTextView;
        private TextView pointsTextView;
        private TextView possibleTextView;


        private Work work;

        public WorkHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            nameTextView = (TextView) itemView.findViewById(R.id.list_item_category_name);
            dueTextView = (TextView) itemView.findViewById(R.id.list_item_category_due);
            pointsTextView = (TextView) itemView.findViewById(R.id.list_item_category_points);
            possibleTextView = (TextView) itemView.findViewById(R.id.list_item_category_possible);


        }


        public void bindWork(Work work) {
            this.work = work;

            SimpleDateFormat df = new SimpleDateFormat("MM/dd");
            DecimalFormat gradef = new DecimalFormat("#.##");

            nameTextView.setText(work.getTitle());
            dueTextView.setText(df.format(work.getDueDate())); // fixed

            if(Double.compare(work.getWeight(),-1.0) == 0){ // not equal weight
                //display the percent for each category equally distributed
                if(Category.EXAM == work.getCategory()){
                    possibleTextView.setText(gradef.format(course.getExam_weight()/course.getExams().size()) + "%");
                }
                else if(Category.QUIZ == work.getCategory()){
                    possibleTextView.setText(gradef.format(course.getQuiz_weight()/course.getQuzs().size()) + "%");
                }
                else if(Category.ASSIGNMENT == work.getCategory()){
                    possibleTextView.setText(gradef.format(course.getAssignment_weight()/course.getAssigs().size()) + "%");
                }
                else if(Category.PROJECT == work.getCategory()){
                    possibleTextView.setText(gradef.format(course.getProject_weight()/course.getProjs().size()) + "%");
                }
                else if(Category.EXTRA == work.getCategory()){
                    possibleTextView.setText(gradef.format(course.getExtra_weight()/course.getExtra().size()) + "%");
                }
            }
            else {
                possibleTextView.setText(gradef.format(work.getWeight()) + "%");
            }

            if(work.getCompleteness() == false){
                pointsTextView.setText("N/A");
            }else{
                pointsTextView.setText(gradef.format(work.getEarned_points()) + "/"
                        + gradef.format(work.getPossible_points()));
            }

        }


        @Override
        public void onClick(View view) {

            startActivityForResult(IndividualActivity.newIntent(getActivity(), work.getTitle(), categoryName, courseId), REQUEST_CODE_CREATE_WORK);
//????
//            Intent intent = IndividualActivity.newIntent(getActivity(), work.getTitle());
//
//            startActivityForResult(intent, REQUEST_CODE_CREATE_WORK);
        }
    }


    private class WorkAdapter extends RecyclerView.Adapter<WorkHolder> implements ItemTouchHelperAdapter{
        private List<Work> works;

        public WorkAdapter(List<Work> works) {
            this.works = works;
        }

        public void setWorks(List<Work> works) {
            this.works = works;
        }

        @Override
        public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_category, parent, false);
            return new WorkHolder(view);
        }

        @Override
        public void onBindViewHolder(WorkHolder holder, int position) {
            Work work = works.get(position);
            holder.bindWork(work);
        }

        @Override
        public int getItemCount() {
            return works.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(works, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(works, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            courseService.removeWorkFromACourse(position, course, category);
            notifyItemRemoved(position);
        }
    }


    public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private final ItemTouchHelperAdapter mAdapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to remove this course?")
                    .setPositiveButton(R.string.confirm_remove, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            mAdapter.onItemDismiss(position);
                            updateUI();
                        }
                    })
                    .setNegativeButton(R.string.cancel_remove, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateUI();
                            // do nothing
                            updateUI();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }

    public void onBackPressed() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }
}


