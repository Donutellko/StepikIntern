package com.donutellko.stepikintern.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.donutellko.stepikintern.api.Course;

import java.util.ArrayList;
import java.util.List;

public class StepikDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "stepik.db";

    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase readableDb;
    private SQLiteDatabase writableDb;

    public StepikDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        readableDb = getReadableDatabase();
        writableDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("SQLite", "Creating database.");

        String SQL_CREATE_COURSE = "CREATE TABLE " + CourseContract.CourseEntry.TABLE_NAME + " (" +
                CourseContract.CourseEntry.COLUMN_ID + " INTEGER, " +
                CourseContract.CourseEntry.COLUMN_TITLE + " TEXT, " +
                CourseContract.CourseEntry.COLUMN_COVER + " TEXT, " +
                CourseContract.CourseEntry.COLUMN_OWNER + " INTEGER, " +
                "" +
                "CONSTRAINT COURSE_PK PRIMARY KEY(" + CourseContract.CourseEntry.COLUMN_ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Course> getStarred() {
        Log.i("SQLite", "Getting starred courses.");

        final String SQL_SELECT_STARRED = "SELECT * FROM " + CourseContract.CourseEntry.TABLE_NAME + ";";

        Cursor cursor = readableDb.rawQuery(SQL_SELECT_STARRED, null);

        int idColumnId = cursor.getColumnIndex(CourseContract.CourseEntry.COLUMN_ID);
        int idColumnTitle = cursor.getColumnIndex(CourseContract.CourseEntry.COLUMN_TITLE);
        int idColumnCover = cursor.getColumnIndex(CourseContract.CourseEntry.COLUMN_COVER);
        int idColumnOwner = cursor.getColumnIndex(CourseContract.CourseEntry.COLUMN_OWNER);

        List<Course> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnId);
            int owner = cursor.getInt(idColumnOwner);
            String title = cursor.getString(idColumnTitle);
            String cover = cursor.getString(idColumnCover);
            list.add(new Course(id, owner, title, cover));
        }

        Log.i("SQLite", "Got " + list.size() + " courses from database.");

        return list;
    }

    public void insertStarred(Course course) {
        Log.i("SQLite", "Adding course to starred.");

        String SQL_INSERT_STARRED = "INSERT INTO " + CourseContract.CourseEntry.TABLE_NAME +
                "(" +
                CourseContract.CourseEntry.COLUMN_ID + ", " +
                escapeSql(CourseContract.CourseEntry.COLUMN_TITLE) + ", " +
                escapeSql(CourseContract.CourseEntry.COLUMN_COVER) + ", " +
                CourseContract.CourseEntry.COLUMN_OWNER + " " +
                ") VALUES (" +
                course.getId() + ", " +
                "\'" + course.getCourseTitle() + "\', " +
                "\'" + course.getCourseCover() + "\', " +
                course.getCourseOwner() + " " +
                ");";

        try {
            writableDb.execSQL(SQL_INSERT_STARRED);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private String escapeSql(String columnCover) {
        columnCover = columnCover.replace("\'", "\'\'");
        return columnCover;
    }

    public void removeStarred(Course course) {
        Log.i("SQLite", "Removing course from starred.");

        String SQL_DELETE_STARRED = "DELETE FROM " + CourseContract.CourseEntry.TABLE_NAME +
                " WHERE " + CourseContract.CourseEntry.COLUMN_ID + "=" + course.getId();

        try {
            writableDb.execSQL(SQL_DELETE_STARRED);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}
