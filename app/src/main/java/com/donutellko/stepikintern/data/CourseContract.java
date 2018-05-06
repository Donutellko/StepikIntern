package com.donutellko.stepikintern.data;

import android.provider.BaseColumns;

public class CourseContract {

    public static final class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "STARRED_COURSE";

        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_TITLE = "TITLE";
        public static final String COLUMN_COVER = "COURSE_COVER";
        public static final String COLUMN_OWNER = "OWNER";
        public static final String COLUMN_SCORE = "SCORE";
    }
}
