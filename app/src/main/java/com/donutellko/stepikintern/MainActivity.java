package com.donutellko.stepikintern;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;

import com.donutellko.stepikintern.JsonHelper.RequestResponse.Course;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.list_recycler);

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), isPortrait ? 2 : 4));

        ArrayList<Course> courseList = new ArrayList<>();

        findViewById(R.id.list_progress).setVisibility(View.VISIBLE);
        courseList.add(new Course("Быстрый старт в Android-разработку"));
        courseList.add(new Course("Web-технологии"));
        courseList.add(new Course("Базы данных"));
        courseList.add(new Course("Политические процессы в современной России"));
        courseList.add(new Course("Электроника и схематехника аналоговых устройств"));
        for (int i = 1; i < 10; i++) courseList.add(new Course("Курс " + i));
        findViewById(R.id.list_progress).setVisibility(View.GONE);

        CourseListAdapter adapter = new CourseListAdapter(courseList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }
}
