package edu.umd.cs.gradeculator.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static edu.umd.cs.gradeculator.service.impl.GradeculatorDbSchema.*;

/**
 * Created by howardksw1 on 4/30/17.
 */

public class GradeculatorCourseDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "gradecular_course.db";
    private static final int VERSION = 1;

    private static final String SQL_CREATE_COURSE = "create table " + CourseTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            CourseTable.Columns.ID + ", " +
            CourseTable.Columns.TITLE + ", " +
            CourseTable.Columns.IDENTIFIER + ", " +
            CourseTable.Columns.EXAM_WEIGHT + ", " +
            CourseTable.Columns.ASSIGNMENT_WEIGHT + ", " +
            CourseTable.Columns.QUIZ_WEIGHT + ", " +
            CourseTable.Columns.PROJECT_WEIGHT + ", " +
            CourseTable.Columns.EXTRA_WEIGHT + ", " +
            CourseTable.Columns.CURRENT_GRADE + ", " +
            CourseTable.Columns.DESIRED_GRADE + ", " +
            CourseTable.Columns.DESIRED_LETTER_GRADE + ", " +
            CourseTable.Columns.CREDITS + ", " +
            CourseTable.Columns.EQUAL_WEIGHT_EXAM + ", " +
            CourseTable.Columns.EQUAL_WEIGHT_ASSIGNMENT + ", " +
            CourseTable.Columns.EQUAL_WEIGHT_PROJECT + ", " +
            CourseTable.Columns.EQUAL_WEIGHT_QUIZ + ", " +
            CourseTable.Columns.EQUAL_WEIGHT_EXTRA +
            ")";

    private static final String SQL_CREATE_WORK = "create table " + WorkTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            WorkTable.Columns.ID + ", " +
            WorkTable.Columns.CATEGORY + ", " +
            WorkTable.Columns.TITLE + ", " +
            WorkTable.Columns.WEIGHT + ", " +
            WorkTable.Columns.EARNED_POINTS + ", " +
            WorkTable.Columns.POSSIBLE_POINTS + ", " +
            WorkTable.Columns.CURRENT_GRADE + ", " +
            WorkTable.Columns.COMPLETENESS + ", " +
            WorkTable.Columns.DUE_DATE +
            ", FOREIGN KEY ("+ WorkTable.Columns.ID +
            ") REFERENCES " + CourseTable.NAME + "(" + CourseTable.Columns.ID +
            "))";

    public GradeculatorCourseDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSE);
        db.execSQL(SQL_CREATE_WORK);

        // kick in the relationship between couse table and work table
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
