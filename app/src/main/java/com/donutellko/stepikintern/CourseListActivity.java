package com.donutellko.stepikintern;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class CourseListActivity extends AppCompatActivity {

    protected TextView textView;
    protected ProgressBar progressBar;
    protected RecyclerView recyclerView;
    protected Button refreshButton;
    protected FloatingActionButton starredFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView  = findViewById(R.id.list_recycler);
        textView      = findViewById(R.id.list_text);
        progressBar   = findViewById(R.id.list_progress);
        refreshButton = findViewById(R.id.list_refresh);
        starredFab    = findViewById(R.id.starred);

        prepareRecyclerView(); // настроить RecycleView: установить количество колонок
    }

    /**
     * Задаёт параметры для RecyclerView: количество колонок.
     */
    private void prepareRecyclerView() {
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), isPortrait ? 2 : 4));
    }

    protected void setVisibility(boolean recycler, boolean progress, boolean text, boolean refresh, boolean starred) {
        setRecyclerVisible(recycler);
        setProgressVisible(progress);
        setTextVisible(text);
        setRefreshVisible(refresh);
        setFabVisible(starred);
    }

    void setRecyclerVisible(boolean recyclerVisible) {
        recyclerView.setVisibility(recyclerVisible ? View.VISIBLE : View.INVISIBLE);
    }

    void setProgressVisible(boolean progressVisible) {
        progressBar.setVisibility(progressVisible ? View.VISIBLE : View.GONE);
    }

    void setTextVisible(boolean textVisible) {
        textView.setVisibility(textVisible ? View.VISIBLE : View.GONE);
    }

    void setRefreshVisible(boolean refreshVisible) {
        refreshButton.setVisibility(refreshVisible ? View.VISIBLE : View.INVISIBLE);
    }

    void setFabVisible(boolean fabVisible) {
        starredFab.setVisibility(fabVisible ? View.VISIBLE : View.INVISIBLE);
    }

}
