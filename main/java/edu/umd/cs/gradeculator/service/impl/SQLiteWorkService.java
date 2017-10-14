package edu.umd.cs.gradeculator.service.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.umd.cs.gradeculator.model.Course;
import edu.umd.cs.gradeculator.model.Work;

import static edu.umd.cs.gradeculator.service.impl.GradeculatorDbSchema.WorkTable;

/**
 * Created by howardksw1 on 5/1/17.
 */

public class SQLiteWorkService {
    private SQLiteDatabase db;

    public SQLiteWorkService(SQLiteDatabase db) {
        this.db = db;
    }

    private class WorkCursorWrapper extends CursorWrapper {
        WorkCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Work getWorksForCourse() {
            String id = getString(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.ID));
            String title = getString(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.TITLE));
            String category = getString(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.CATEGORY));
            double weight = getDouble(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.WEIGHT));
            double earned_point = getDouble(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.EARNED_POINTS));
            double possible_point= getDouble(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.POSSIBLE_POINTS));
            String completeness= getString(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.COMPLETENESS));
            String due_date = getString(getColumnIndex(GradeculatorDbSchema.WorkTable.Columns.DUE_DATE));

            Work work = new Work(title);
            work.setCategory(work.toString_Category(category));
            work.setWeight(weight);
            work.setEarned_points(earned_point);
            work.setPossible_points(possible_point);
            if(completeness.equals("true") || completeness.equals("True")) {
                work.setCompleteness();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                work.setDueDate(sdf.parse(due_date));
            }catch (ParseException e) {
                Log.d("SQLiteWorkServce", "Parse error");
            }
            return work;
        }
    }

    private ArrayList<Work> queryWork(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = db.query(WorkTable.NAME, null,
                whereClause, whereArgs, null, null, null);

        WorkCursorWrapper wrapper = new WorkCursorWrapper(cursor);
        ArrayList<Work> works = new ArrayList<>();

        try {
            wrapper.moveToFirst();
            while(!cursor.isAfterLast()) {
                works.add(wrapper.getWorksForCourse());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return works;
    }

    public Work getWorksById(String id, String title) {
        if(id != null && title != null) {
            String[] whereArgs = new String[]{id, title};

            ArrayList<Work> works = queryWork(WorkTable.Columns.ID + " =? AND "
                    + WorkTable.Columns.TITLE + " =?", whereArgs, null);
            if(works.size() != 0) {
                return works.get(0);
            }
        }
        return null;
    }

    public void addWorkToBacklog(Course course, Work target, String title) {
        if(getWorksById(course.getId(), target.getTitle()) != null && target.getTitle().equals(title)) {
            // title is not changed
            db.update(WorkTable.NAME, getContentValues(course, target), WorkTable.Columns.ID +
                            " = ? AND " + WorkTable.Columns.TITLE + "=?",
                    new String[]{course.getId(), target.getTitle()});

        }else if(title != null && getWorksById(course.getId(), title) != null) {
            // This means the user change the title of work because title is used as
            // a unique ID in the table
            db.insert(WorkTable.NAME, null, getContentValues(course,target));
            db.delete(WorkTable.NAME, WorkTable.Columns.ID + "= ? AND " +
            WorkTable.Columns.TITLE + " = ? ", new String[]{course.getId(), title});
        } else {
            db.insert(WorkTable.NAME, null, getContentValues(course, target));
        }
    }

    public boolean removeWorkFromBacklog(int position, Course course, Work.Category category) {
        ArrayList<Work> works = null;
        switch (category) {
            case EXAM:
                works = course.getExams();
                break;
            case ASSIGNMENT:
                works = course.getAssigs();
                break;
            case PROJECT:
                works = course.getProjs();
                break;
            case QUIZ:
                works = course.getQuzs();
                break;
            case EXTRA:
                works = course.getExtra();
                break;
        }
        Work work = works.get(position);

        if(getWorksById(course.getId(), work.getTitle()) != null) {
            if(db.delete(WorkTable.NAME, WorkTable.Columns.ID + "=? AND " +
                    WorkTable.Columns.TITLE + "=?", new String[]{course.getId(), work.getTitle()}) != 0) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Work> getAllWorks_ForCourse(Course course) {
        ArrayList<Work> works =
                queryWork(WorkTable.Columns.ID + "=?", new String[]{course.getId()}, null);

        return works;
    }

    private static ContentValues getContentValues(Course course, Work work) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.ID, (course.getId()));
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.TITLE, work.getTitle());
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.CATEGORY,
                work.toCategory_String(work.getCategory()));
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.WEIGHT, work.getWeight());
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.EARNED_POINTS, work.getEarned_points());
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.POSSIBLE_POINTS, work.getPossible_points());
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.COMPLETENESS, work.getCompleteness().toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        contentValues.put(GradeculatorDbSchema.WorkTable.Columns.DUE_DATE, df.format(work.getDueDate()));
        return contentValues;
    }
}
