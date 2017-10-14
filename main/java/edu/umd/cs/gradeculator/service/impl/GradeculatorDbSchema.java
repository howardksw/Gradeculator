package edu.umd.cs.gradeculator.service.impl;

/**
 * Created by howardksw1 on 4/30/17.
 */

public class GradeculatorDbSchema {
    public static final class CourseTable {
        public static final String NAME = "gradecular_course";

        public static final class Columns {
            public static final String ID = "ID";
            public static final String TITLE = "TITLE";
            public static final String IDENTIFIER = "IDENTIFIER";
            public static final String EXAM_WEIGHT = "EXAM_WEIGHT";
            public static final String ASSIGNMENT_WEIGHT = "ASSIGNMENT_WEIGHT";
            public static final String QUIZ_WEIGHT = "QUIZ_WEIGHT";
            public static final String PROJECT_WEIGHT = "PROJECT_WEIGHT";
            public static final String EXTRA_WEIGHT = "EXTRA_WEIGHT";
            public static final String CURRENT_GRADE = "CURRENT_GRADE";
            public static final String DESIRED_GRADE = "DESIRED_GRADE";
            public static final String DESIRED_LETTER_GRADE = "DESIRED_LETTER_GRADE";
            public static final String CREDITS = "CREDITS";
            public static final String EQUAL_WEIGHT_EXAM = "EQUAL_WEIGHT_EXAM";
            public static final String EQUAL_WEIGHT_ASSIGNMENT = "EQUAL_WEIGHT_ASSIGNMENT";
            public static final String EQUAL_WEIGHT_PROJECT = "EQUAL_WEIGHT_PROJECT";
            public static final String EQUAL_WEIGHT_QUIZ = "EQUAL_WEIGHT_QUIZ";
            public static final String EQUAL_WEIGHT_EXTRA = "EQUAL_WEIGHT_EXTRA";

        }
    }

    public static final class WorkTable {
        public static final String NAME = "gradecular_work";

        public static final class Columns {
            public static final String ID = "ID";
            public static final String CATEGORY = "CATEGORY";
            public static final String TITLE = "TITLE";
            public static final String WEIGHT = "WEIGHT";
            public static final String EARNED_POINTS = "EARNED_POINTS";
            public static final String POSSIBLE_POINTS = "POSSIBLE_POINTS";
            public static final String CURRENT_GRADE = "CURRENT_GRADE";
            public static final String COMPLETENESS = "COMPLETENESS";
            public static final String DUE_DATE = "DUE_DATE";
        }
    }
}
